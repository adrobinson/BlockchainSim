package blockchainsim;

import java.util.ArrayList;

public class Node {

    ArrayList<Transaction> mempool = new ArrayList<>(); // The mempool will store unconfirmed transactions to be validated by the nodes.
    Blockchain blockchain;

    public Node(Blockchain blockchain){
        this.blockchain = blockchain;
    }

    public boolean verifyTransaction(Transaction tx) throws Exception {
        if (!tx.verifySignature()) { // If signature cannot be verified, display message and return
            System.out.println("Invalid Transaction, signature not verified");
            return false;
        }

        String sender = tx.getSender();
        double senderAmount = tx.getBlockchain().getUtxo_pool().getOrDefault(sender, 0.0); // Check sender has sufficient funds

        if (senderAmount < tx.getAmount()) {
            System.out.println("Cannot make transaction, insufficient funds.");
            return false;
        }

        return true;
    }

    public void storeTransaction(Transaction tx){
        mempool.add(tx); // transaction will be verified by other nodes before adding to mempool
    }

    public ArrayList<Transaction> getMempool() {
        return mempool;
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }
}
