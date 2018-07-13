package _do.com.humano.service.impl;

import _do.com.humano.service.ImageService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by jjmendoza on 12/7/2018.
 */
@Service
public class ImageServiceImpl implements ImageService {
    @Override
    public void base64ImageToFile(String base64Img, String filePath) {
        System.out.println("base64Img: " + base64Img);
        byte[] imageByte = Base64Utils.decodeFromString(base64Img.replaceFirst("data:image/.*;base64,", ""));

        try (FileOutputStream fos = new FileOutputStream(filePath); FileChannel fc = fos.getChannel()) {
            ByteBuffer buffer = ByteBuffer.wrap(imageByte);
            fc.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Image base64ImageToImage(String base64Img) {
        byte[] imageByte = Base64Utils.decodeFromString(base64Img.replaceFirst("data:image/.*;base64,", ""));
        Image image = null;
        try {
            image = ImageIO.read(new ByteArrayInputStream(imageByte));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    @Override
    public Image getQRCodeImage(String data, int width, int height) {
        Image image = null;
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            image = ImageIO.read(new ByteArrayInputStream(pngOutputStream.toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return image;
    }
}
