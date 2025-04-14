package blockchainsim;

import org.checkerframework.checker.units.qual.A;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;

public class CoinbaseTransaction extends Transaction{
    public CoinbaseTransaction(){
        this.outputs = new ArrayList<>();
    }
    public CoinbaseTransaction(PublicKey receiver, double amount){
        this.receiver = Base64.getEncoder().encodeToString(receiver.getEncoded());
        this.amount = amount;
        this.timestamp = System.currentTimeMillis();
        this.transactionID = TransactionUtil.generateID("Coinbase", this.receiver, amount, this.timestamp);
        this.outputs = new ArrayList<>();
        addOutput(amount, this.receiver);
    }
}
