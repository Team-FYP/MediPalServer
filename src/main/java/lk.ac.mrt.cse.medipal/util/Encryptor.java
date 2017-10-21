package lk.ac.mrt.cse.medipal.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lakshan on 10/21/17.
 */
public class Encryptor {
    public static String encryptMD5(String plainText) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        byte[] bytesOfMessage = plainText.getBytes("UTF-8");

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] cipherBytes = md.digest(bytesOfMessage);

        return new String(cipherBytes);
    }
}
