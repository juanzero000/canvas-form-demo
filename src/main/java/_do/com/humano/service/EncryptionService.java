package _do.com.humano.service;

/**
 * Created by jjmendoza on 13/7/2018.
 */
public interface EncryptionService {
    String encrypt(String data, String key);
    String decrypt(String encryptedData, String key);
}
