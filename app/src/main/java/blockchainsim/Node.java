package blockchainsim;

import java.util.ArrayList;

public class Node {

    ArrayList<Transaction> mempool = new ArrayList<>(); // The mempool will store unconfirmed transactions to be validated by the nodes.
    Blockchain blockchain;

    public Node(Blockchain blockchain){
        this.blockchain = blockchain;
    }

    protected boolean verifyTransaction(Transaction tx) throws Exception {
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

    public Blockchain getBlockchain() {
        return blockchain;
    }
}
