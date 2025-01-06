package com.example.image_detection.util;

import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageSource;
import com.google.protobuf.ByteString;
import org.apache.logging.log4j.util.Strings;

import java.io.FileInputStream;
import java.io.IOException;

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
}
