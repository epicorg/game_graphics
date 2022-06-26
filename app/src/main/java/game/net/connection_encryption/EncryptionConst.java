package game.net.connection_encryption;

/**
 * @author Noris
 * @version 2
 * @date 30/05/2015
 */
public class EncryptionConst {

    public static final String ASYMMETRIC_ALGORITHM = "RSA";
    public static final int ASYMMETRIC_KEY_SIZE = 512;
    public static final String ASYMMETRIC_DECODE = ASYMMETRIC_ALGORITHM + "/ECB/PKCS1Padding";

    public static final String SYMMETRIC_ALGORITHM = "AES";
    public static final int SYMMETRIC_KEY_SIZE = 128;

}
