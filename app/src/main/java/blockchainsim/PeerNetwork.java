package blockchainsim;

import java.util.ArrayList;

public class PeerNetwork {

    ArrayList<Node> nodeList;

    public PeerNetwork(){
        nodeList = new ArrayList<>();
    }

    public void addNode(Node node){
        nodeList.add(node);
    }

    public int broadcastTransaction(Node originNode, PeerTransaction tx) throws Exception {

        int accepted = 0;
        int rejected = 0;

        for(Node node: nodeList) {
            if(node.getBlockchain() == originNode.getBlockchain()){ // Check nodes are a part of the same blockchain
                if (node != originNode){
                    if (!node.verifyTransaction(tx)){
                        rejected++;
                        node.setTransactionAccepted(false);
                    } else {
                        accepted++;
                        node.setTransactionAccepted(true);
                    }
                }
            }
        }

        System.out.println("Accepted: " + accepted + " Rejected: " + rejected);

        if (accepted == 0){ // If all nodes found the transaction to be invalid, return 0
            originNode.clearInvalidTransaction(tx); // remove invalid transaction from node that broadcasted it
            return 0;
        }

        if (rejected > accepted){ // If majority of nodes reject the transaction, some nodes will need to rectify transaction.
            return 1;
        }

        for(Node node: nodeList) {
            node.storeTransaction(tx);
        }
        return 2; // majority/all nodes found transaction to be true
    }

    public void rectifyTransaction(PeerTransaction tx){
        for(Node node: nodeList){
            if(node.isTransactionAccepted()){ // If node rejected transaction, do not run the clear function
                node.clearInvalidTransaction(tx);
            }
        }
    }

    public int broadcastBlock(Node originNode, Block block){
        int accepted = 0;
        int rejected = 0;

        for(Node node: nodeList) {
            if(node.getBlockchain() == originNode.getBlockchain()){ // Check nodes are a part of the same blockchain
                if (node != originNode){
                    if (!node.verifyBlock(block)){
                        rejected++;
                    } else {
                        accepted++;
                    }
                }
            }
        }

        System.out.println("Accepted: " + accepted + " Rejected: " + rejected);

        if (accepted == 0){ // If all nodes found the transaction to be invalid, return 0
            return 0;
        }

        if (rejected > accepted){ // If majority of nodes reject the transaction, some nodes will need to rectify transaction.
            return 1;
        }

        for(Node node: nodeList) {
            node.addBlock(block);
        }
        return 2; // majority/all nodes found transaction to be true
    }

    public ArrayList<Node> getNodeList() {
        return nodeList;
    }
}
