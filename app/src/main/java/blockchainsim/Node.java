package blockchainsim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.prefs.PreferenceChangeListener;

public class Node {

    private ArrayList<Transaction> mempool = new ArrayList<>(); // The mempool will store unconfirmed transactions to be validated by the nodes.
    private LinkedHashMap<UTXO, Character> pendingUTXOs = new LinkedHashMap<>(); // LinkedHashMap so we can track insertion order and UTXO outputIndex (prevent conflicts)
    private HashMap<String, UTXO> utxoPool;
    private Blockchain blockchain;

    public Node(Blockchain blockchain){
        this.blockchain = blockchain;
    }

    public boolean verifyTransaction(Transaction tx) throws Exception {
        if (!tx.verifySignature()) { // If signature cannot be verified, display message and return
            System.out.println("Invalid Transaction, signature not verified");
            return false;
        }

        String sender = tx.getSender();
        double senderAmount = 0;
        double senderRemainder;

        utxoPool = blockchain.getUtxo_pool();
        ArrayList<String> results = new ArrayList<>(); // will store the utxoID's

        for(String utxoID: utxoPool.keySet()){
            if(Objects.equals(utxoPool.get(utxoID).getRecipientAddress(), sender)) { // Look through utxoPool to find unspent currency for sender
                results.add(utxoID);
                senderAmount += utxoPool.get(utxoID).getAmount();
                if (senderAmount >= tx.getAmount()) { // if sender has enough to complete transaction
                    senderRemainder = senderAmount % tx.getAmount(); // get the remainder of the currency after the transaction
                    pendingUTXOs.put(new UTXO(tx.getTransactionID(), 0, sender, senderRemainder),  '+'); // we keep the outputIndex as '0' until it is added to the actual utxo pool.
                    for(String result: results) {
                        System.out.println(result);
                        pendingUTXOs.put(utxoPool.get(result), '-'); // These transactions will be removed from the UTXO pool once the transaction has been added to the blockchain
                    }
                    pendingUTXOs.put(new UTXO(tx.getTransactionID(), 0, tx.getReceiver(), tx.getAmount()), '+'); // The recipient to be added to the UTXO pool
                    storeTransaction(tx);
                    return true;
                }
            }
        }


        if (senderAmount < tx.getAmount()) {
            System.out.println("Cannot make transaction, insufficient funds.");
            return false;
        }

        return true;
    }

    public void storeTransaction(Transaction tx){
        mempool.add(tx); // transaction will be verified by other nodes before adding to mempool
    }

    public ArrayList<Transaction> getMempool() {
        return mempool;
    }

    public HashMap<UTXO, Character> getPendingUTXOs() {
        return pendingUTXOs;
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }
}
