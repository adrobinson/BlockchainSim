package blockchainsim;

import java.util.ArrayList;

public class Transaction {
    protected String receiver;
    protected double amount;
    protected long timestamp;
    protected String transactionID;
    protected ArrayList<Object> outputs = new ArrayList<>();

    public String getReceiver() {return receiver;}
    public String getSender(){return null;}
    public double getAmount() {return amount;}
    public String getTransactionID() {return transactionID;}

    public ArrayList<Object> getOutputs() {return outputs;}
    public void addOutput(double value, String address){
        outputs.add(value);
        outputs.add(address);
    }
}
