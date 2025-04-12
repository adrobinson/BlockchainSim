package blockchainsim;

import java.io.File;
import java.util.*;

public class Node {

    private ArrayList<Transaction> mempool = new ArrayList<>(); // The mempool will store unconfirmed transactions to be validated by the nodes.
    private ArrayList<UTXO> pendingUTXOs = new ArrayList<>(); // LinkedHashMap so we can track insertion order and UTXO outputIndex (prevent conflicts)
    private ArrayList<String> tempResultIDs = new ArrayList<>();
    private ArrayList<String> toBeSpent = new ArrayList<>();
    private HashMap<String, UTXO> utxoPool;
    private Blockchain blockchain;
    private boolean transactionAccepted;

    public Node(Blockchain blockchain){
        this.blockchain = blockchain;
        utxoPool = blockchain.getUtxo_pool();
    }


    public boolean verifyTransaction(PeerTransaction tx) throws Exception {
        if (!tx.verifySignature()) { // If signature cannot be verified, display message and return
            System.out.println("Invalid Transaction, signature not verified");
            return false;
        }

        toBeSpent.clear();

        double senderAmount = BCProtocol.verifySenderFunds(this, utxoPool, tx.getSender(), tx.getAmount());
        System.out.println(senderAmount);
        if (senderAmount < tx.getAmount()){
            System.out.println("Cannot make transaction, insufficient funds.");
            return false;
        }

        // If sender has funds:
        pendingUTXOs.add(new UTXO(tx.getTransactionID(), toBeSpent, tx.getReceiver(), tx.getAmount())); // transaction to be added to utxo pool

        double senderRemainder = tx.getAmount() % senderAmount;
        if (senderRemainder > 0.0){
            pendingUTXOs.add(new UTXO(tx.getTransactionID(), utxoPool.get(toBeSpent.getLast()).getOutputIndex(), tx.getSender(), senderRemainder));
        } // If sender has remaining funds, add them back to utxo pool. Left over currency will inherit the output index of the utxo they are from
        markUTXOsAsPending(tx);

        return true;
    }

    public void markUTXOsAsPending(Transaction tx){
        for(UTXO utxo: pendingUTXOs) {
            if(Objects.equals(utxo.getTransactionID(), tx.getTransactionID()))
                    for(String id: utxo.getOutputIndex()){
                        if(utxoPool.get(id) != null){
                            utxoPool.get(id).setPending(true); // marked as pending so double spending doesn't occur
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
        for(UTXO utxo: pendingUTXOs){
            if(Objects.equals(utxo.getTransactionID(), tx.getTransactionID())){ // get pending utxo's that reference the transaction
                for(String id: utxo.getOutputIndex()){
                    if(utxoPool.get(id) != null) {
                        utxoPool.get(id).setPending(false); // set pending to false so utxo can be spent
                    }
                }
                pendingUTXOs.remove(utxo);
            }

        }


    }

    public void setTransactionAccepted(boolean accepted){
        this.transactionAccepted = accepted;
    }

    public boolean isTransactionAccepted() {
        return transactionAccepted;
    }

    public void addToBeSpent(String utxoID){
        toBeSpent.add(utxoID);
    }

    public ArrayList<Transaction> getMempool() {
        return mempool;
    }

    public ArrayList<UTXO> getPendingUTXOs() {
        return pendingUTXOs;
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }
}
