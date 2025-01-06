package com.example.image_detection.util;

import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageSource;
import com.google.protobuf.ByteString;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;

@Service
public class ImageUtility {

    public Image getImageFromSource(String path) throws IOException {
        if (Strings.isBlank(path)) return null;
        if (path.startsWith("http")) {
            ImageSource imgSource = ImageSource.newBuilder().setImageUri(path).build();
            return Image.newBuilder().setSource(imgSource).build();
        } else {
            ByteString imgBytes = ByteString.readFrom(new FileInputStream(path));
            return Image.newBuilder().setContent(imgBytes).build();
        }
    }
}
