package blockchainsim;

import java.time.LocalDateTime;

public class Block {
    private int index;
    private LocalDateTime timestamp;
    private String previousHash;
    private String data;
    private int nonce;
    private String hash;

    public Block(int index, String previousHash, String data) {
        this.index = index;
        this.previousHash = previousHash;
        this.data = data;
        this.timestamp = LocalDateTime.now();
        this.nonce = 0;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String input = index + timestamp.toString() + previousHash + data + nonce;
        return Encryptor.encryptString(input);
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
