package blockchainsim;

public class Miner extends Node{

    public Miner(Blockchain blockchain) {
        super(blockchain);
    }

    public void mineBlock(Block targetBlock) {

        String target = "0".repeat(targetBlock.getDifficulty());

        while (!targetBlock.calculateHash().startsWith(target)){
            targetBlock.setNonce(targetBlock.getNonce() + 1);
        }

        targetBlock.setHash(targetBlock.calculateHash());
        System.out.println("Block Mined");
    }
}
