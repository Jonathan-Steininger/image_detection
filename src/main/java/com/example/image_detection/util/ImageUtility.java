package com.example.image_detection.util;

import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageSource;
import com.google.protobuf.ByteString;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

//@Service
public class ImageUtility {

    public static Image getImageFromSource(String path) {
        if (Strings.isBlank(path)) return null;

        try {
            if (path.startsWith("http")) {
                ImageSource imgSource = ImageSource.newBuilder().setImageUri(path).build();
                return Image.newBuilder().setSource(imgSource).build();
            } else {
                ByteString imgBytes = ByteString.readFrom(new FileInputStream(path));
                return Image.newBuilder().setContent(imgBytes).build();
            }
        } catch (IOException e) {
            e.printStackTrace(); // todo handle better
            return null;
        }
    }

    public static byte[] compressImage(byte[] data) { // todo delete me

        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4 * 1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch (Exception e) {
        }
        return outputStream.toByteArray();
    }

    public static byte[] decompressImage(byte[] data) {// todo delete me
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4 * 1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception exception) {
        }
        return outputStream.toByteArray();
    }
}
