package blockchainsim;

import java.util.HashMap;
import java.util.Objects;

public final class BCProtocol {

    private BCProtocol(){
    }

    public static boolean verifyBlockHash(Block targetBlock) {
        if (!targetBlock.getHash().startsWith("0".repeat(targetBlock.getDifficulty()))) {
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
    public static double verifySenderFunds(Node node, HashMap<Integer, UTXO> utxoPool, PeerTransaction tx){

        double senderAmount = 0;
        boolean addInputs = true;

        if(!tx.getInputs().isEmpty()){
            addInputs = false;
        }

        for(int index: utxoPool.keySet()){
            if(!utxoPool.get(index).isPending()) { // If utxo is pending, it cannot be used
                if (Objects.equals(utxoPool.get(index).getAddress(), tx.getSender())) { // Look through utxoPool to find unspent currency for sender

                    if (addInputs) {
                        tx.addInput(utxoPool.get(index).getTransactionID(), index);
                    } else {
                        if(!tx.getInputs().contains(utxoPool.get(index).getTransactionID()) && tx.getInputs().contains(index)) {
                            System.out.println("Inconsistent inputs"); // TODO handle
                        }
                    }

                    senderAmount += utxoPool.get(index).getAmount();

                    if (senderAmount >= tx.getAmount()) { // if sender has enough to complete transaction
                        return senderAmount;
                    }
                }
            }
        }
        return 0.0;
    }

}
