package zm.irc.util;

import org.apache.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Md5Util {
    private static final Logger log = Logger.getLogger(Md5Util.class);
    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };


    public static String toMd5(String str){
        if(str == null){
            return null;
        }
        try {
            MessageDigest alg = MessageDigest.getInstance("MD5");
            return byteArrayToHexString(alg.digest(str.getBytes(StandardCharsets.UTF_8)));
        }catch (Exception e){
            log.error("Get Instance Error!",e);
        }
        return null;
    }


    private static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte value : b) {
            resultSb.append(byteToHexString(value));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
}
