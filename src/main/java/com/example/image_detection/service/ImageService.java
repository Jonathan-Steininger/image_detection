package com.example.image_detection.service;

import com.example.image_detection.data.entity.ImageEntity;
import com.example.image_detection.data.model.UploadRequestModel;
import com.example.image_detection.data.repository.ImageRepository;
import com.google.cloud.vision.v1.Image;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    @Transactional
    public ImageEntity upload(Image image, UploadRequestModel uploadRequestModel) {
        ImageEntity imageEntity = mapToEntity(image, uploadRequestModel);
        imageRepository.save(imageEntity);
        return imageEntity;
    }

    public ImageEntity getImageWithImageId(Long imageId) {
        if (imageId == null) return null;
        return imageRepository.findById(imageId).orElse(null);
    }

    private ImageEntity mapToEntity(Image image, UploadRequestModel uploadRequestModel) {
        String label = Strings.isBlank(uploadRequestModel.getLabel()) ? UUID.randomUUID().toString() : uploadRequestModel.getLabel();
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setLabel(label);
        imageEntity.setImageBytes(getImageBytes(image));

        return imageEntity;
    }

    private byte[] getImageBytes(Image image) {
        if (image == null) return null;
        if (image.getContent() == null || image.getContent().isEmpty()) return null;
        return image.getContent().toByteArray();
    }
}
