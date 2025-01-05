package com.example.image_detection.service;

import com.example.image_detection.data.entity.ImageEntity;
import com.example.image_detection.data.model.UploadRequest;
import com.example.image_detection.data.repository.ImageRepository;
import com.example.image_detection.util.ImageUtility;
import com.google.cloud.vision.v1.Image;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageEntity processImageUploadRequest(UploadRequest uploadRequest) {
        if (uploadRequest == null) return null;
        if (Strings.isBlank(uploadRequest.getImage())) return null;

        Image image = ImageUtility.getImageFromSource(uploadRequest.getImage());
        ImageEntity imageEntity = mapToEntity(image, uploadRequest);
        saveImage(imageEntity);
        try {
            DetectLabelsGcs.detectLabels(image, uploadRequest.getEnableObjectDetection());
        } catch (IOException e) {
            log.error(e.getMessage()); // todo handle better
        }
        return imageEntity;
    }

    private ImageEntity mapToEntity(Image image, UploadRequest uploadRequest) {
        if (image == null) return null;
        if (image.getContent() == null || image.getContent().isEmpty()) return null;
        String label = Strings.isBlank(uploadRequest.getLabel()) ? UUID.randomUUID().toString() : uploadRequest.getLabel();

        return ImageEntity.builder()
                .label(label)
                .imageBytes(image.getContent().toByteArray())
                .build();
    }

    private void saveImage(ImageEntity imageEntity) {
        if (imageEntity == null) return;
        imageRepository.save(imageEntity);
    }
}
