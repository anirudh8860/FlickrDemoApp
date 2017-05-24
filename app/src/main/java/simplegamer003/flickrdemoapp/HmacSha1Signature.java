package simplegamer003.flickrdemoapp;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Anirudh on 24-05-2017.
 */

public class HmacSha1Signature {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();

        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        String s = formatter.toString();
        return formatter.toString();
    }

    public static String calculateRFC2104HMAC(String data, String key) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        return toHexString(mac.doFinal(data.getBytes("ASCII")));
    }

}
