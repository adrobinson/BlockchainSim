/*
 * This source file was generated by the Gradle 'init' task
 */
package blockchainsim;

import java.security.PublicKey;
import java.util.Map;

public class App {

    public static void main(String[] args) throws Exception {
        Block block = new Block(0, "None");

        Blockchain currency = Blockchain.getInstance();

        Wallet Bob = new Wallet();
        Wallet Alice = new Wallet();

        PeerNetwork peerNetwork = new PeerNetwork();

        Node node1 = new Node(currency);
        peerNetwork.addNode(node1);
        Node node2 = new Node(currency);
        peerNetwork.addNode(node2);
        Node node3 = new Node(currency);
        peerNetwork.addNode(node3);
        Node node4 = new Node(currency);
        peerNetwork.addNode(node4);

        Miner miner = new Miner(currency);


        currency.getUtxo_pool().put("abcd1234", new UTXO("hashhash", 0, Bob.getPublicAddress(), 5.0));
        currency.getUtxo_pool().put("hjfdu6236", new UTXO("asdhask", 1, Bob.getPublicAddress(), 7.50));

        for(UTXO value: currency.getUtxo_pool().values()){
            System.out.println("Wallet Address: " + value.getAddress() + " Balance: " + value.getAmount());
        }

        System.out.println("Attempting transaction Bob -> Alice (10)");
        Transaction tx1 = new Transaction((PublicKey) Bob.getPublicKey(), (PublicKey) Alice.getPublicKey(), currency, 10);
        tx1.signTransaction(Bob.getPrivateKey());
        Transaction tx2 = new Transaction((PublicKey) Bob.getPublicKey(), (PublicKey) Alice.getPublicKey(), currency, 10);

        if (node1.verifyTransaction(tx1)){
            if(peerNetwork.broadcastTransaction(node1 ,tx1) == 2){
                System.out.println("Transaction validated and stored in mempool");
            } else if(peerNetwork.broadcastTransaction(node1 ,tx1) == 1) {
                System.out.println("Majority of nodes found this transaction to be invalid! ");
                peerNetwork.rectifyTransaction(tx1);
            } else {
                System.out.println("All nodes found this transaction to be invalid!");
            }
        }

        System.out.println("Pending UTXO's: ");
        for(Map.Entry<UTXO, Character> entry: node1.getPendingUTXOs().entrySet()){
            System.out.println("Address: " + entry.getKey().getAddress() + " Amount: " + entry.getKey().getAmount() + " Add/Remove: " + entry.getValue() + " " + entry.getKey().getTransactionID());
        }

        System.out.println("Adding to block");
        System.out.println("Block Hash: " + block.getHash());
        block.addTransaction(tx1);
        System.out.println("Block Hash: " + block.getHash());
        block.addTransaction(tx2);
        System.out.println("Block Hash: " + block.getHash());


        for(Node node: peerNetwork.getNodeList()){
            System.out.println(node + "Transactions stored: " + node.getMempool());
            System.out.println("Pending UTXOs: " + node.getPendingUTXOs());
        }

        System.out.println("Block Data: " + block.getData());

    }



}
