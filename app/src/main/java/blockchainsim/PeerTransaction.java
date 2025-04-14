package blockchainsim;

import org.checkerframework.checker.units.qual.A;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;

public class PeerTransaction extends Transaction {
    private String sender;
    private byte[] signature;
    private ArrayList<Object> inputs;

    public PeerTransaction(){
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
    }

    public PeerTransaction(PublicKey sender, PublicKey receiver, double amount){
        this.sender = Base64.getEncoder().encodeToString(sender.getEncoded());
        this.receiver = Base64.getEncoder().encodeToString(receiver.getEncoded());
        this.amount = amount;
        this.timestamp = System.currentTimeMillis();
        this.transactionID = TransactionUtil.generateID(this.sender, this.receiver, this.amount, this.timestamp);
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
    }

    public void signTransaction(PrivateKey privateKey) throws Exception{
        String data = sender + receiver + amount; // signature tied to this transaction
        Signature dsa = Signature.getInstance("SHA256withECDDSA", "BC");
        dsa.initSign(privateKey);
        dsa.update(data.getBytes());
        this.signature = dsa.sign();
    }

    public boolean verifySignature() throws Exception{
        String data = sender + receiver + amount; // Recreate transaction data
        Signature dsa = Signature.getInstance("SHA256withECDSA", "BC"); // Initialize ECDSA, same algorithm we signed with

        // Convert Base64 public key back to actual PublicKey object
        PublicKey publicKey = KeyFactory.getInstance("ECDSA")
                .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(sender)));

        dsa.initVerify(publicKey); // Set public key for verification
        dsa.update(data.getBytes()); // Hash the transaction data

        return dsa.verify(signature); // Check if signature is valid
    }

    public String getSender() {return sender;}

    public ArrayList<Object> getInputs() {return inputs;}

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setInputs(ArrayList<Object> inputs) {
        this.inputs = inputs;
    }

    public void addInput(String txId, int outputIndex){
        inputs.add(txId);
        inputs.add(outputIndex);
    }

}
