package blockchainsim;

import java.security.Key;
import java.security.PrivateKey;
import java.util.Base64;

public class WalletData {
    private String publicKey;
    private String privateKey;

    public WalletData(){}

    public WalletData(Key pubKey, PrivateKey privKey){
        this.publicKey = Base64.getEncoder().encodeToString(pubKey.getEncoded());
        this.privateKey = Base64.getEncoder().encodeToString(privKey.getEncoded());
    }

    public String getPrivateKey() {
        return privateKey;
    }
    public String getPublicKey() {return publicKey;}

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
