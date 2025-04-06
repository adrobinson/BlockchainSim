package blockchainsim;

import java.security.PublicKey;
import java.util.Base64;

public class CoinbaseTransaction extends Transaction{
    public CoinbaseTransaction(PublicKey receiver, double amount){
        this.receiver = Base64.getEncoder().encodeToString(receiver.getEncoded());
        this.amount = amount;
        this.timestamp = System.currentTimeMillis();
        this.transactionID = TransactionUtil.generateID("Coinbase", this.receiver, amount, this.timestamp);
    }
}
