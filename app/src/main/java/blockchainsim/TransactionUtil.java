package blockchainsim;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TransactionUtil {

    public static String generateID (String sender, String reciever, double amount, long timestamp) {
        String data = sender + reciever + amount + timestamp;
        return Encryptor.encryptString(data); // Use exisiting hash algorithm

    }



}
