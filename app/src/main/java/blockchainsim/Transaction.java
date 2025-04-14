package blockchainsim;

import java.util.ArrayList;

public class Transaction {
    protected String receiver;
    protected double amount;
    protected long timestamp;
    protected String transactionID;
    protected ArrayList<Object> outputs;

    public String getReceiver() {return receiver;}
    public double getAmount() {return amount;}
    public String getTransactionID() {return transactionID;}

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setOutputs(ArrayList<Object> outputs) {
        this.outputs = outputs;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ArrayList<Object> getOutputs() {return outputs;}
    public void addOutput(double value, String address){
        outputs.add(value);
        outputs.add(address);
    }
}
