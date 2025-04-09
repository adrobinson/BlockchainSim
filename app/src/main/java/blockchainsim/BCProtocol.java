package blockchainsim;

import java.util.HashMap;
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
    public static double verifySenderFunds(Node node, HashMap<String, UTXO> utxoPool, String sender, double amount){

        double senderAmount = 0;

        for(String utxoID: utxoPool.keySet()){
            if(!utxoPool.get(utxoID).isPending()) { // If utxo is pending, it cannot be used
                if (Objects.equals(utxoPool.get(utxoID).getAddress(), sender)) { // Look through utxoPool to find unspent currency for sender

                    node.addResult(utxoID);
                    senderAmount += utxoPool.get(utxoID).getAmount();

                    if (senderAmount >= amount) { // if sender has enough to complete transaction
                        return senderAmount;
                    }
                }
            }
        }
        return 0.0;
    }

}
