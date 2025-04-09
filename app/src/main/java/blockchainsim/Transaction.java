package blockchainsim;

public class Transaction {
    protected String receiver;
    protected double amount;
    protected Blockchain blockchain;
    protected long timestamp;
    protected String transactionID;

    public String getReceiver() {return receiver;}
    public double getAmount() {return amount;}
    public String getTransactionID() {return transactionID;}

    public Blockchain getBlockchain() {return blockchain;}
}
