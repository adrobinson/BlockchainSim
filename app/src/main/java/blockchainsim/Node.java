package blockchainsim;

import java.io.File;
import java.util.*;

public class Node {

    private ArrayList<Transaction> mempool = new ArrayList<>(); // The mempool will store unconfirmed transactions to be validated by the nodes.
    private ArrayList<Integer> outputIndexes = new ArrayList<>();
    private LinkedHashMap<Integer, UTXO> utxoPool;
    private Blockchain blockchain;
    private boolean transactionAccepted;
    private int index = 0;

    public Node(Blockchain blockchain){
        this.blockchain = blockchain;
        utxoPool = new LinkedHashMap<>();
    }

    // Method for verifying peer transactions
    public boolean verifyTransaction(PeerTransaction tx) throws Exception {
        if (!tx.verifySignature()) { // If signature cannot be verified, display message and return
            System.out.println("Invalid Transaction, signature not verified");
            return false;
        }

        outputIndexes.clear();

        double senderAmount = BCProtocol.verifySenderFunds(this, utxoPool, tx);
        System.out.println(senderAmount);
        if (senderAmount < tx.getAmount()){
            System.out.println("Cannot make transaction, insufficient funds.");
            return false;
        }

        // If sender has funds:
        tx.addOutput(tx.getAmount(), tx.getReceiver());


        double senderRemainder = tx.getAmount() % senderAmount;
        if (senderRemainder > 0.0){
            tx.addOutput(senderRemainder, tx.getSender());
        } // If sender has remaining funds, add them back to utxo pool. Left over currency will inherit the output index of the utxo they are from
        markUTXOsAsPending(tx);

        return true;
    }

    // Method for verifying coinbase transactions
    public boolean verifyTransaction(CoinbaseTransaction tx){
        if(tx.getAmount() > blockchain.getRewardLimit()){
            return false;
        }

        tx.addOutput(tx.getAmount(), tx.getReceiver());
        return true;

    }

    public void markUTXOsAsPending(PeerTransaction tx){
        for(Object input: tx.getInputs()){
            if(input instanceof Integer){
                utxoPool.get(input).setPending(true);
            }
        }
    }
    public void clearInvalidTransaction(PeerTransaction tx){
        for(Object input: tx.getInputs()){
            if(input instanceof Integer){
                utxoPool.get(input).setPending(false);
            }
        }
    }
    public void storeTransaction(Transaction tx){
        mempool.add(tx); // transaction will be verified by other nodes before adding to mempool
    }

    // If a transaction is found to be invalid by majority nodes, nodes that falsely validated it will run this method


    public void setTransactionAccepted(boolean accepted){
        this.transactionAccepted = accepted;
    }

    public boolean isTransactionAccepted() {
        return transactionAccepted;
    }

    public ArrayList<Transaction> getMempool() {
        return mempool;
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }

    public void addUTXOtoPool(UTXO utxo){
        utxoPool.put(index, utxo);
        index++;
    }

    public void addOutputIndex(int out){
        outputIndexes.add(out);
    }
}
