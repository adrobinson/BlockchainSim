package blockchainsim;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.util.*;

public class Node {

    private ArrayList<Transaction> mempool = new ArrayList<>(); // The mempool will store unconfirmed transactions to be validated by the nodes.
    private ArrayList<Integer> outputIndexes = new ArrayList<>();
    private LinkedHashMap<Integer, UTXO> utxoPool;
    private LinkedList<Block> blockchainCopy;
    private Blockchain blockchain;
    private boolean transactionAccepted;
    private int index = 0;

    public Node(Blockchain blockchain){
        this.blockchain = blockchain;
        utxoPool = new LinkedHashMap<>();
        blockchainCopy = new LinkedList<>();
        buildLocalChainData("app/src/main/resources/BlockData");
    }

    public void buildLocalChainData(String pathToBlockDirectory){
        File directory = new File(pathToBlockDirectory);
        if(directory.exists() && directory.isDirectory()){
            File[] files = directory.listFiles();

            if(files != null){

                System.out.println(Arrays.toString(files));
                for(File file: files) { // read all block files
                    Block block = BlockUtil.readBlock(file);
                    if (BCProtocol.verifyBlockHash(block)){
                        updatePool(block.getData());
                        blockchainCopy.add(block);
                    } else {
                        System.out.println("Block read is invalid");
                    }
                }
            } else {
                System.out.println("This directory is empty");
            }
        } else {
            System.out.println("This directory doesn't exist");
        }
    }



    // Method for verifying peer transactions
    public boolean verifyTransaction(PeerTransaction tx) {
        if (!tx.verifySignature()) { // If signature cannot be verified, display message and return
            System.out.println("Invalid Transaction, signature not verified");
            return false;
        }

        outputIndexes.clear();

        double senderAmount = BCProtocol.verifySenderFunds(this, utxoPool, tx);
        System.out.println(senderAmount);
        if (senderAmount < tx.getAmount()){
            System.out.println("Cannot make transaction, insufficient funds.");
            return false;
        }

        if(mempool.contains(tx) || !tx.getOutputs().isEmpty()){ // If node is verifying a block, this transaction should already be stored, meaning no outputs need to be added.
            return true;
        }

        // If sender has funds:
        tx.addOutput(tx.getAmount(), tx.getReceiver());

        double senderRemainder = senderAmount - tx.getAmount();
        if (senderRemainder > 0.0){
            tx.addOutput(senderRemainder, tx.getSender());
        } // If sender has remaining funds, add them back to utxo pool. Left over currency will inherit the output index of the utxo they are from
        markUTXOsAsPending(tx);

        return true;
    }

    // Method for verifying coinbase transactions
    public boolean verifyTransaction(CoinbaseTransaction tx){
        if(tx.getAmount() > blockchain.getRewardLimit()){
            return false;
        }

        tx.addOutput(tx.getAmount(), tx.getReceiver());
        return true;

    }

    public boolean verifyBlock(Block block){
        if(!BCProtocol.verifyBlockHash(block)){
            return false;
        }

        ArrayList<Transaction> data = block.getData();
        for(int i = 0; i < data.size(); i++) {

            if (i == 0){ // First transaction should always be a coinbase transaction (miner reward)
                if(data.get(i) instanceof CoinbaseTransaction){
                    if(!verifyTransaction((CoinbaseTransaction) data.get(i))){
                        return false;
                    }
                    continue;
                } else {
                    return false;
                }
            }

            if (data.get(i) instanceof PeerTransaction) { // all other transactions should be peer to peer
                PeerTransaction tx = (PeerTransaction) data.get(i);

                if(!verifyTransaction(tx)){
                    return false;
                }

            } else {
                return false;
            }
        }

        return true;
    }

    public void addBlock(Block block){
        updatePool(block.getData());
        blockchainCopy.add(block);
    }

    public void updatePool(ArrayList<Transaction> txList) {
        for (int i = 0; i < txList.size(); i++) { // read all transactions in blocks
            Transaction tx = txList.get(i);
            List<Object> outputs = tx.getOutputs();
            String txId = tx.getTransactionID();

            if(mempool.contains(tx)){
                mempool.remove(tx); // remove any transactions being added to utxo pool from mempool
            }

            // In every output there is a recipient & amount
            String[] recipients = new String[outputs.size() / 2];
            double[] amounts = new double[outputs.size() / 2];
            int index = 0;
            for (int k = 0; k < outputs.size(); k += 2, index++) {
                amounts[index] = Double.parseDouble(outputs.get(k).toString());
                recipients[index] = outputs.get(k + 1).toString(); // Every recipient/amount pair will be recipients[i]/amounts[i]
            }

            if (i == 0) { // first transaction will always be coinbase transaction (no inputs)
                addUTXOtoPool(new UTXO(txId, null, recipients[0], amounts[0])); // coinbase transaction will only ever have 1 output for the 1 miner

            } else {
                List<Object> inputs = ((PeerTransaction) tx).getInputs();
                ArrayList<Integer> outputIndexes = new ArrayList<>();

                for (int j = 0; j < (inputs.size()); j += 2) { // Every second value in inputs is an output index
                    int outputIndex = Integer.parseInt(inputs.get(j + 1).toString());
                    outputIndexes.add(outputIndex);
                    utxoPool.remove(outputIndex);
                }

                for (int l = 0; l < recipients.length; l++) {
                    addUTXOtoPool(new UTXO(txId, outputIndexes, recipients[l], amounts[l]));
                }
            }
        }
    }

    public void markUTXOsAsPending(PeerTransaction tx){
        for(Object input: tx.getInputs()){
            if(input instanceof Integer){
                utxoPool.get(input).setPending(true);
            }
        }
    }
    public void clearInvalidTransaction(PeerTransaction tx){
        for(Object input: tx.getInputs()){
            if(input instanceof Integer){
                utxoPool.get(input).setPending(false);
            }
        }
    }
    public void storeTransaction(Transaction tx){
        mempool.add(tx); // transaction will be verified by other nodes before adding to mempool
    }

    // If a transaction is found to be invalid by majority nodes, nodes that falsely validated it will run this method


    public void setTransactionAccepted(boolean accepted){
        this.transactionAccepted = accepted;
    }

    public boolean isTransactionAccepted() {
        return transactionAccepted;
    }

    public ArrayList<Transaction> getMempool() {
        return mempool;
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }

    public void addUTXOtoPool(UTXO utxo){
        utxoPool.put(index, utxo);
        index++;
    }

    public void addOutputIndex(int out){
        outputIndexes.add(out);
    }

    public LinkedHashMap<Integer, UTXO> getUtxoPool() {
        return utxoPool;
    }
}
