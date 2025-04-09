package blockchainsim;

import java.util.*;

public class Node {

    private ArrayList<Transaction> mempool = new ArrayList<>(); // The mempool will store unconfirmed transactions to be validated by the nodes.
    private LinkedHashMap<UTXO, Character> pendingUTXOs = new LinkedHashMap<>(); // LinkedHashMap so we can track insertion order and UTXO outputIndex (prevent conflicts)
    private ArrayList<String> tempResultIDs = new ArrayList<>();
    private ArrayList<String> results = new ArrayList<>();
    private HashMap<String, UTXO> utxoPool;
    private Blockchain blockchain;
    private boolean transactionAccepted;

    public Node(Blockchain blockchain){
        this.blockchain = blockchain;
        utxoPool = blockchain.getUtxo_pool();
    }

    //TODO Move verification methods into a consensus class
    public boolean verifyTransaction(PeerTransaction tx) throws Exception {
        if (!tx.verifySignature()) { // If signature cannot be verified, display message and return
            System.out.println("Invalid Transaction, signature not verified");
            return false;
        }

        results.clear();
        tempResultIDs.clear(); // Clear this array after transaction been fully verified

        double senderAmount = BCProtocol.verifySenderFunds(this, utxoPool, tx.getSender(), tx.getAmount());
        System.out.println(senderAmount);
        if (senderAmount < tx.getAmount()){
            System.out.println("Cannot make transaction, insufficient funds.");
            return false;
        }

        // If sender has funds:
        pendingUTXOs.put(new UTXO(tx.getTransactionID(), 0, tx.getReceiver(), tx.getAmount()), '+'); // transaction to be added to utxo pool

        double senderRemainder = tx.getAmount() % senderAmount;
        if (senderRemainder > 0.0){ pendingUTXOs.put(new UTXO(tx.getTransactionID(), 0, tx.getSender(), senderRemainder), '+'); } // If sender has remaining funds, add them back to utxo pool

        for (String id: results) {
            pendingUTXOs.put(utxoPool.get(id), '-'); // mark any funds that were used to be removed
            tempResultIDs.add(utxoPool.get(id).getTransactionID());
        }

        markUTXOsAsPending();
        return true;
    }

    public void markUTXOsAsPending(){
        for (UTXO utxo: pendingUTXOs.keySet()){ // Setting their pending var to true will make sure they can't be used in other transactions
            for(String utxoID: utxoPool.keySet()){
                if(Objects.equals(utxoPool.get(utxoID), utxo)){
                    utxoPool.get(utxoID).setPending(true);
                }
            }
        }
    }

    public void storeTransaction(Transaction tx){
        mempool.add(tx); // transaction will be verified by other nodes before adding to mempool
    }

    public void removeTransaction(Transaction tx) {
        mempool.remove(tx);
    }

    public void removePendingUTXO(UTXO utxoToRemove){
        pendingUTXOs.remove(utxoToRemove);
    }

    // If a transaction is found to be invalid by majority nodes, nodes that falsely validated it will run this method
    public void clearInvalidTransaction(Transaction tx){
        List<UTXO> toRemove = new ArrayList<>();
        for(UTXO utxo: pendingUTXOs.keySet()){
                if (Objects.equals(utxo.getTransactionID(), tx.getTransactionID()) || tempResultIDs.contains(utxo.getTransactionID())){
                    toRemove.add(utxo);
                }
        }

        if(toRemove.getFirst() != null){
            for(UTXO utxo: toRemove){
                removePendingUTXO(utxo);
            }
        }


    }

    public void setTransactionAccepted(boolean accepted){
        this.transactionAccepted = accepted;
    }

    public boolean isTransactionAccepted() {
        return transactionAccepted;
    }

    public void addResult(String utxoID){
        results.add(utxoID);
    }

    public void addPending(UTXO utxo, char action){
        pendingUTXOs.put(utxo, action);
    }

    public ArrayList<Transaction> getMempool() {
        return mempool;
    }

    public HashMap<UTXO, Character> getPendingUTXOs() {
        return pendingUTXOs;
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }
}
