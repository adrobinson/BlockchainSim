package blockchainsim;

import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Transaction {
    private String sender;
    private String receiver;
    private double amount;
    private byte[] signature;
    private Blockchain blockchain;

    public Transaction(PublicKey sender, PublicKey receiver, Blockchain blockchain, double amount){
        this.sender = Base64.getEncoder().encodeToString(sender.getEncoded());
        this.receiver = Base64.getEncoder().encodeToString(receiver.getEncoded());
        this.amount = amount;
        this.blockchain = blockchain;
    }


    public String getSender() {return sender;}
    public String getReceiver() {return receiver;}
    public double getAmount() {return amount;}

    public Blockchain getBlockchain() {return blockchain;}
}
