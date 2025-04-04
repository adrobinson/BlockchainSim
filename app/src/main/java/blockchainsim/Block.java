package blockchainsim;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Block {
    private int index;
    private LocalDateTime timestamp;
    private String previousHash;
    private ArrayList<Transaction> data = new ArrayList<>();
    private int dataLimit = 5;
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

        System.out.println("input" + input);
        return Encryptor.encryptString(String.valueOf(input));
    }

    public void addTransaction(Transaction tx) {
        if (data.size() < dataLimit){
            data.add(tx);
            this.hash = calculateHash(); // Block rehashed when new data is added
        }
        else{System.out.println("Block has reached data limit!");}
    }

    public void setNonce(int nonce) {this.nonce = nonce;}
    public void setHash(String hash) {this.hash = hash;}
    public void setPreviousHash(String previousHash) {this.previousHash = previousHash;}

    public int getIndex() {return index;}
    public String getPreviousHash() {return previousHash;}
    public ArrayList<Transaction> getData() {return data;}
    public LocalDateTime getTimestamp() {return timestamp;}
    public int getNonce() {return nonce;}
    public String getHash() {return hash;}

}
