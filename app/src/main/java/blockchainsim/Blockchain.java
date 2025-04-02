package blockchainsim;

import java.util.LinkedList;

public class Blockchain extends LinkedList<Block> {
    int difficulty;
    Block genesisBlock;
    public Blockchain(int difficulty) {
        this.difficulty = difficulty;
    }

    public boolean add(Block block){
        // when a block is to be added to the blockchain, it will first be checked by the consensus class
        return false;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
