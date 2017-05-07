import sun.misc.BASE64Encoder;

import java.security.MessageDigest;

/**
 * Created by zhihan on 2/7/17.
 */
final class PasswordManager {
    private PasswordManager() {}
    static final int ENCRYPT_ERROR = -7;

    /**
     * encrypt password using MD5.
     * @param plaintext password.
     * @return encrypted char array.
     */
    static char[] encrypt(char[] plaintext){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(new String(plaintext).getBytes("UTF-8"));
            byte[] raw = md.digest();
            return (new BASE64Encoder().encode(raw)).toCharArray();
        } catch (Exception e) {
            System.err.println("[ERROR] Cannot encrypt " + e);
            return null;
        }
    }
}
