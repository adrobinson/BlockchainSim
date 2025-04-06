package blockchainsim;

import java.util.Objects;

public final class BCProtocol {

    static Blockchain blockchain = Blockchain.getInstance();

    private BCProtocol(){
    }

    public static boolean verifyBlockHash(Block targetBlock) {
        if (!targetBlock.getHash().startsWith("0".repeat(blockchain.getDifficulty()))) {
            System.out.println("Block hash does not match difficulty");
            return false;
        }
        System.out.println("Block hash matches difficulty");

        if(!Objects.equals(targetBlock.calculateHash(), targetBlock.getHash())){
            System.out.println("Proof of work invalid");
            return false;
        }
        System.out.println("Proof of work validated");

        return true;
    }

}
