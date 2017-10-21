package lk.ac.mrt.cse.medipal.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lakshan on 10/21/17.
 */
public class Encryptor {
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(Encryptor.class);

    public static String encryptMD5(String plainText) {
        String cipherText = DigestUtils
                .md5Hex(plainText).toUpperCase();
        return cipherText;
    }

    public static boolean verifyPassword(String plainText, String hash){
        String cipherText = DigestUtils
                .md5Hex(plainText).toUpperCase();
        if (hash.equals(cipherText)){
            return true;
        }
        return false;
    }
}
