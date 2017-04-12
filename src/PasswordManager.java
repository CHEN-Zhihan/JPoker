import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public synchronized boolean authenticate(char[] plaintext, char[] password) {
        char[] encrypted = this.encrypt(plaintext);
        return Arrays.equals(encrypted, password);
    }

    synchronized static PasswordManager  getInstance() {
        if (instance == null) {
            instance = new PasswordManager();
        }
        return instance;
    }
}
