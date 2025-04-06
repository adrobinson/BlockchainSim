package blockchainsim;

import java.security.PublicKey;
import java.util.Base64;

public class CoinbaseTransaction{
    private String receiver;
    private double rewardAmount;

    public CoinbaseTransaction(PublicKey receiver, double rewardAmount){
        this.receiver = Base64.getEncoder().encodeToString(receiver.getEncoded());
        this.rewardAmount = rewardAmount;
    }

}
