package dev.fullstackcode.otp.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Service
public class QRCodeService {

    @Autowired
    QRCodeWriter qrCodeWriter;

    public byte[] generateQRCode(String secret) throws WriterException {

        //     QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        BitMatrix bitMatrix = qrCodeWriter.encode(secret,
                BarcodeFormat.QR_CODE, 200, 200,
                hints);


        BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(qrCodeImage, "png", outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write QR code image to output stream.", e);
        }

        return outputStream.toByteArray();
    }
}
