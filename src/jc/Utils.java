package jc;

import sun.misc.BASE64Decoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ��� on 2015/7/26.
 */
public class Utils {

    public static String getSafeFolderName(String folderName){

        String regex = "[\\\\/:\\*?<>|\\.$\\[\\]\\s~]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(folderName);

        while (matcher.find()){
            folderName = matcher.replaceAll("");
        }

        return folderName;
    }

    public static String getReverseHexString(String password){

        StringBuffer stringBuffer = new StringBuffer(password);
        StringBuffer result = new StringBuffer();
        stringBuffer.reverse();
        for (int i = 0 ; i< stringBuffer.length() ; i++){
             result.append(Integer.toHexString(stringBuffer.charAt(i)));
        }

        return result.toString();
    }
}
