package game.net.connection_encryption;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Generate a symmetric {@link java.security.Key} using {@link KeyGenerator}.
 *
 * @author Noris
 * @date 27/04/2015
 * @see KeyGenerator
 */
public class SymmetricKeyGenerator implements ISymmetricKeyGenerator {

    private SecretKey key;

    public void generateKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(EncryptionConst.SYMMETRIC_ALGORITHM);
            keyGenerator.init(EncryptionConst.SYMMETRIC_KEY_SIZE);
            key = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public SecretKey getKey() {
        return key;
    }

}
