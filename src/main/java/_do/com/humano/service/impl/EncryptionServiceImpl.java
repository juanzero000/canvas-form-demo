package _do.com.humano.service.impl;

import _do.com.humano.service.EncryptionService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by jjmendoza on 13/7/2018.
 */
@Service
public class EncryptionServiceImpl implements EncryptionService {

    @Value("${humano.init-vector}")
    private String initVector;
    private static final String charset = "UTF-8";
    private static final String algorithm = "AES";
    private static final String cipherTransformation = "AES/CBC/PKCS5PADDING";
    private static final int MAX_KEY_LENGTH = 16;

    @Override
    public String encrypt(String data, String key) {
        try {
            Cipher cipher = getCipher(prepareKey(key), Cipher.ENCRYPT_MODE);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String decrypt(String encryptedData, String key) {
        try {
            Cipher cipher = getCipher(prepareKey(key), Cipher.DECRYPT_MODE);
            byte[] original = cipher.doFinal(Base64.decodeBase64(encryptedData));
            return new String(original);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Cipher getCipher(String key, final int opmode) {
        try {
            System.out.println("initVector: " + initVector);
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(charset));
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(charset), algorithm);

            Cipher cipher = Cipher.getInstance(cipherTransformation);
            cipher.init(opmode, secretKeySpec, iv);
            return cipher;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private String prepareKey(String key) {
        if (key.length() > MAX_KEY_LENGTH)
            return key.substring(0, MAX_KEY_LENGTH);

        return key.concat(key.substring(0, MAX_KEY_LENGTH - key.length()));
    }
}
