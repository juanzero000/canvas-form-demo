package _do.com.humano.service;


import java.awt.*;

/**
 * Created by jjmendoza on 12/7/2018.
 */
public interface ImageService {

    void base64ImageToFile(String base64Img, String filePath);

    Image base64ImageToImage(String base64Img);

    Image getQRCodeImage(String data, int width, int height);

}
