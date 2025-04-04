package blockchainsim;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Blockchain extends LinkedList<Block> {
    int difficulty;
    Block genesisBlock;
    LinkedHashMap<String, UTXO> utxo_pool = new LinkedHashMap<>();


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

    public void addUtxo(String utxoID, UTXO utxo){
        utxo_pool.put(utxoID, utxo);
    }

    public void removeUtxo(String utxoID){
        utxo_pool.remove(utxoID);
    }

    public LinkedHashMap<String, UTXO> getUtxo_pool() {return utxo_pool;}
}
