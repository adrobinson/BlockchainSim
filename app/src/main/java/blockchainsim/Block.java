package blockchainsim;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

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
    private String formattedData;

    @JsonCreator
    public Block(JsonNode index, JsonNode timestamp, JsonNode previousHash, JsonNode hash, JsonNode nonce, JsonNode diff){
        this.index = index.asInt();
        this.timestamp = timestamp.asLong();
        this.previousHash = previousHash.asText();
        this.hash = hash.asText();
        this.nonce = nonce.asInt();
        this.difficulty = diff.asInt();
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
        String input = index + timestamp.toString() + previousHash + formattedData + nonce;

        return Encryptor.encryptString(String.valueOf(input));
    }

    public void addTransaction(PeerTransaction tx) {
        if (!data.isEmpty()){
            if (data.size() < dataLimit){
                data.add(tx);
                this.formattedData = BlockUtil.formatData(data);
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
    public void setTimestamp(Long timestamp) {this.timestamp = timestamp;}
    public void setDifficulty(int difficulty) {this.difficulty = difficulty;}
    public void setIndex(int index) {this.index = index;}

    public int getIndex() {return index;}
    public String getPreviousHash() {return previousHash;}
    public ArrayList<Transaction> getData() {return data;}
    public Long getTimestamp() {return timestamp;}
    public int getNonce() {return nonce;}
    public String getHash() {return hash;}

    public int getDifficulty() {return difficulty;}
}
