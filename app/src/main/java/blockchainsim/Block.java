package blockchainsim;

import java.time.LocalDateTime;

public class Block {
    private int index;
    private LocalDateTime timestamp;
    private String previousHash;
    private String data;
    private int nonce;
    private String hash;

    public Block(int index, String previousHash) {
        this.index = index;
        this.previousHash = previousHash;
        this.timestamp = LocalDateTime.now();
        this.nonce = 0;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String input = index + timestamp.toString() + previousHash + data + nonce;
        return Encryptor.encryptString(input);
    }

    public boolean addTransaction(Transaction tx) throws Exception{
        if (!tx.verifySignature()) {
            System.out.println("Invalid Transaction, signature not verified");
            return false;
        }

        String sender = tx.getSender();
        String receiver = tx.getReceiver();
        double senderAmount = tx.getBlockchain().getUtxo_pool().getOrDefault(sender, 0.0); // Find amount that sender has already

        if (senderAmount < tx.getAmount()) {
            System.out.println("Cannot make transaction, insufficient funds.");
            return false;
        }

        tx.getBlockchain().getUtxo_pool().put(sender, senderAmount - tx.getAmount()); // update the sender and receiver's amounts in the utxo pool
        tx.getBlockchain().getUtxo_pool().put(receiver, tx.getBlockchain().getUtxo_pool().getOrDefault(receiver, 0.0) + tx.getAmount());

        this.data += (tx);
        return true;
    }

    public void setNonce(int nonce) {this.nonce = nonce;}
    public void setHash(String hash) {this.hash = hash;}
    public void setPreviousHash(String previousHash) {this.previousHash = previousHash;}

    public int getIndex() {return index;}
    public String getPreviousHash() {return previousHash;}
    public String getData() {return data;}
    public LocalDateTime getTimestamp() {return timestamp;}
    public int getNonce() {return nonce;}
    public String getHash() {return hash;}

}
