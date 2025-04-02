package blockchainsim;

import java.security.*;
import java.util.UUID;

public class Wallet {
    private KeyPairGenerator kpg;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private String publicAddress;

    public Wallet() {

        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
            KeyPair pair = kpg.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
            System.out.println("Keys generated");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        this.publicAddress = UUID.randomUUID().toString();
        publicAddress = publicAddress.replace("-", "");
    }

    protected Key getPublicKey(){
        if (publicKey != null ) {
            return publicKey;
        }
        return null;
    }
    protected String getPublicAddress(){
        return publicAddress;
    }


}
