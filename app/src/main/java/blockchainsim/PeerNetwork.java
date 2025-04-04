package blockchainsim;

import blockchainsim.Node;
import blockchainsim.Transaction;

import java.util.ArrayList;

public class PeerNetwork {

    ArrayList<Node> nodeList;

    public PeerNetwork(){
        nodeList = new ArrayList<>();
    }

    public void addNode(Node node){
        nodeList.add(node);
    }

    public boolean broadcastTransaction(Node originNode, Transaction tx) throws Exception {

        for(Node node: nodeList) {
            if(node.getBlockchain() == originNode.getBlockchain()){ // Check nodes are a part of the same blockchain
                if (node != originNode){
                    if (!node.verifyTransaction(tx)){
                        return false;
                    }
                }
            }
        }

        for(Node node: nodeList) {
            node.storeTransaction(tx);
        }
        return true;
    }

    public ArrayList<Node> getNodeList() {
        return nodeList;
    }
}
