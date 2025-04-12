package blockchainsim;

import java.util.ArrayList;

public class UTXO {
    private boolean pending = false;
    private String transactionID;
    private ArrayList<String> outputIndex;
    private String recipientAddress;
    private double amount;
    public UTXO(String transactionID, ArrayList<String> outputIndex, String recipientAddress, double amount) {
        this.transactionID = transactionID;
        this.outputIndex = outputIndex;
        this.recipientAddress = recipientAddress;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public String getAddress() {
        return recipientAddress;
    }

    public ArrayList<String> getOutputIndex() {return outputIndex;}

    public boolean isPending() {return pending;}

    public void setPending(boolean pending) {
        this.pending = pending;
    }
}
