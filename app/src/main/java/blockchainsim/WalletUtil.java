package blockchainsim;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers.data;

public class WalletUtil {

    public static void storeWallet(Wallet wallet, String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        WalletData walletCopy = new WalletData(wallet.getPublicKey(), wallet.getPrivateKey());

        try{
            mapper.writeValue(new File("app/src/main/resources/WalletData/" + fileName + ".json"), walletCopy);
            System.out.println("Wallet saved to " + fileName + ".json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Wallet loadWallet(String path){
        ObjectMapper mapper = new ObjectMapper();

        try{
            WalletData walletCopy = mapper.readValue(new File(path), WalletData.class); // pass data into wallet data class
            KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", "BC"); // Same algorithm as wallet constructor

            byte[] pubBytes = Base64.getDecoder().decode(walletCopy.getPublicKey());
            byte[] privBytes = Base64.getDecoder().decode(walletCopy.getPrivateKey());

            PublicKey pub = keyFactory.generatePublic(new X509EncodedKeySpec(pubBytes));
            PrivateKey priv = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privBytes));

            return new Wallet(pub, priv);
        } catch (IOException | InvalidKeySpecException e){
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }

        return null;

    }

}
