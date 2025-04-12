package blockchainsim;

public class Transaction {
    protected String receiver;
    protected double amount;
    protected long timestamp;
    protected String transactionID;

    public String getReceiver() {return receiver;}
    public String getSender(){return null;}
    public double getAmount() {return amount;}
    public String getTransactionID() {return transactionID;}

}
