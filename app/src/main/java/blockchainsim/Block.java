package blockchainsim;


import java.util.ArrayList;

public class Block {
    private int index;
    private Long timestamp;
    private String previousHash;
    private ArrayList<Transaction> data = new ArrayList<>();
    private int dataLimit = 5;
    private int nonce;
    private String hash;
    private int difficulty;

    public Block(){

    }

    public Block(int index, String previousHash, int difficulty) {
        this.index = index;
        this.previousHash = previousHash;
        this.timestamp = System.currentTimeMillis();
        this.nonce = 0;
        this.hash = calculateHash();
        this.difficulty = difficulty;
    }

    public String calculateHash() {
        String input = index + timestamp.toString() + previousHash + data + nonce;

        return Encryptor.encryptString(String.valueOf(input));
    }

    public void addTransaction(PeerTransaction tx) {
        if (!data.isEmpty()){
            if (data.size() < dataLimit){
                data.add(tx);
                this.hash = calculateHash(); // Block rehashed when new data is added
            } else {
                System.out.println("Block has reached data limit!");
            }
        } else {
            System.out.println("First transaction in block must be coinbase transaction");
        }

    }

    public void addTransaction(CoinbaseTransaction tx){
        if(data.isEmpty()){
            data.add(tx);
        } else {
            System.out.println("Cannot add more than one Coinbase transaction");
        }

    }

    public void setNonce(int nonce) {this.nonce = nonce;}
    public void setHash(String hash) {this.hash = hash;}
    public void setPreviousHash(String previousHash) {this.previousHash = previousHash;}

    public int getIndex() {return index;}
    public String getPreviousHash() {return previousHash;}
    public ArrayList<Transaction> getData() {return data;}
    public Long getTimestamp() {return timestamp;}
    public int getNonce() {return nonce;}
    public String getHash() {return hash;}

    public int getDifficulty() {return difficulty;}
}
