package com.sih.certificate.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QRCodeUtil {

    public static String generateQRCode(String text, String fileName) {
        try {
            int width = 300;
            int height = 300;

            Path qrDir = Paths.get("qrcodes");
            if (!Files.exists(qrDir)) {
                Files.createDirectories(qrDir);
            }

            BitMatrix matrix = new MultiFormatWriter()
                    .encode(text, BarcodeFormat.QR_CODE, width, height);

            Path filePath = qrDir.resolve(fileName + ".png");
            MatrixToImageWriter.writeToPath(matrix, "PNG", filePath);

            return filePath.toString().replace("\\", "/");

        } catch (Exception e) {
            throw new RuntimeException("QR Code generation failed", e);
        }
    }
}
