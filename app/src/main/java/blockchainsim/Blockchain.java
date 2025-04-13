package blockchainsim;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Blockchain extends LinkedList<Block> {
    private int difficulty = 5;
    private int rewardLimit = 50;
    private Block genesisBlock;
    private int blockHeight = 0;
    private static Blockchain blockchain = new Blockchain();

    private Blockchain(){}
    public static Blockchain getInstance() {
        return blockchain;
    }

    public boolean add(Block block){
        // when a block is to be added to the blockchain, it will first be checked by the consensus class
        return false;
    }

    public int getDifficulty() {return difficulty;}

    public int getBlockHeight() {return blockHeight;}

    public int getRewardLimit() {
        return rewardLimit;
    }
}
