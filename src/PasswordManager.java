import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * Created by zhihan on 2/7/17.
 */
public final class PasswordManager {
    private static PasswordManager instance;
    private PasswordManager() {}

    synchronized char[] encrypt(char[] plaintext){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(new String(plaintext).getBytes("UTF-8"));
            byte[] raw = md.digest();
            return (new BASE64Encoder().encode(raw)).toCharArray();
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    synchronized static PasswordManager  getInstance() {
        if (instance == null) {
            instance = new PasswordManager();
        }
        return instance;
    }
}
