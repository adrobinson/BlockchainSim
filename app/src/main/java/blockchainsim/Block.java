package blockchainsim;


import java.time.LocalDateTime;

public class Block {
    private int index;
    private LocalDateTime timestamp;
    private String previousHash;
    private String data;
    private int nonce;
    private String hash;

    public Block(int index, String previousHash, String data){
        this.index = index;
        this.previousHash = previousHash;
        this.data = data;
        this.timestamp = LocalDateTime.now();
        this.nonce = 0;
    }

    public String calculateHash() {
        String input = index + timestamp.toString() + previousHash + data + nonce;
        return "";
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public String getHash() {
        return hash;
    }

}
