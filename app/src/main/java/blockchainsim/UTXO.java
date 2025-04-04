package blockchainsim;

public class UTXO {
    private String transactionID;
    private int outputIndex;
    private String recipientAddress;
    private double amount;
    public UTXO(String transactionID, int outputIndex, String recipientAddress, int amount) {
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

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public int getOutputIndex() {
        return outputIndex;
    }
}
