package study.project.whereareyou.OtherUsefullClass;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 15/12/2015.
 */
public class MyEncoder {
    public static String encodeFile(String text) throws UnsupportedEncodingException {
        byte[] data = text.getBytes("UTF-8");
        String base64Result = Base64.encodeToString(data,Base64.NO_WRAP);
        return base64Result;
    }
}