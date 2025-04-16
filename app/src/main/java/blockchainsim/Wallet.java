package blockchainsim;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.annotation.Nullable;
import java.security.*;
import java.util.Base64;

public class Wallet {
    private KeyPairGenerator kpg;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public Wallet(PublicKey pubKey, PrivateKey privKey){
        this.publicKey = pubKey;
        this.privateKey = privKey;
    }

    public Wallet() {

        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("ECDSA", "BC");
            kpg.initialize(256, new SecureRandom());

            KeyPair keyPair = kpg.generateKeyPair(); // Generate public/private keys
            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();
            System.out.println("Keys generated");

        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    protected Key getPublicKey() {
        if (publicKey != null) {
            return publicKey;
        }
        return null;
    }

    protected PrivateKey getPrivateKey() {
        if (privateKey != null) {
            return privateKey;
        }
        return null;
    }

    protected String getPublicAddress() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
}