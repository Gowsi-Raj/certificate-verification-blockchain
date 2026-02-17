package com.sih.certificate;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class QRCodeUtil {

    // saves to src/main/resources/static/qrcodes/
   public String generateQrToFile(String text, String fileNameNoExt) throws IOException, WriterException {

    String baseDir = System.getProperty("user.dir");
    File dir = new File(baseDir + "/src/main/resources/static/qrcodes");
    if (!dir.exists()) dir.mkdirs();

    String fileName = fileNameNoExt + ".png";
    File file = new File(dir, fileName);

    int width = 300;
    int height = 300;

    BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
    MatrixToImageWriter.writeToPath(bitMatrix, "PNG", file.toPath());

    // store only the filename in DB
    return fileName;
}

}
