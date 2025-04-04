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
    }

    public boolean verifyTransaction(Transaction tx) throws Exception {
        if (!tx.verifySignature()) { // If signature cannot be verified, display message and return
            System.out.println("Invalid Transaction, signature not verified");
            return false;
        }

        String sender = tx.getSender();
        double senderAmount = 0;
        double senderRemainder;

        utxoPool = blockchain.getUtxo_pool();
        results.clear();
        tempResultIDs.clear(); // Clear this array after transaction been fully verified

        for(String utxoID: utxoPool.keySet()){
            if(Objects.equals(utxoPool.get(utxoID).getAddress(), sender)) { // Look through utxoPool to find unspent currency for sender

                results.add(utxoID);
                senderAmount += utxoPool.get(utxoID).getAmount();

                if (senderAmount >= tx.getAmount()) { // if sender has enough to complete transaction

                    senderRemainder = senderAmount % tx.getAmount(); // get the remainder of the currency after the transaction

                    if (senderRemainder > 0.0){ // Only add to pendingUTXOs if sender has currency leftover
                        pendingUTXOs.put(new UTXO(tx.getTransactionID(), 0, sender, senderRemainder),  '+'); // we keep the outputIndex as '0' until it is added to the actual utxo pool.
                    }

                    for(String result: results) {
                        pendingUTXOs.put(utxoPool.get(result), '-'); // These transactions will be removed from the UTXO pool once the transaction has been added to the blockchain
                        tempResultIDs.add(utxoPool.get(result).getTransactionID()); // Will be used to remove pending UTXO's if found invalid by other nodes
                    }

                    pendingUTXOs.put(new UTXO(tx.getTransactionID(), 0, tx.getReceiver(), tx.getAmount()), '+'); // The recipient to be added to the UTXO pool
                    return true;
                }
            }
        }

        // If the sender has insufficient funds after the utxo pool is checked, the transaction is abandoned
        if (senderAmount < tx.getAmount()) {
            System.out.println("Cannot make transaction, insufficient funds.");
            return false;
        }

        return true;
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

        if(toRemove != null){
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
