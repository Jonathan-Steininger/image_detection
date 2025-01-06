package com.example.image_detection.service;

import com.example.image_detection.data.entity.ImageEntity;
import com.example.image_detection.data.model.UploadRequestModel;
import com.example.image_detection.data.repository.ImageRepository;
import com.google.cloud.vision.v1.Image;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ObjectService objectService;

    @Transactional
    public ImageEntity upload(Image image, UploadRequestModel uploadRequestModel) {
        ImageEntity imageEntity = mapToEntity(image, uploadRequestModel);
        saveImage(imageEntity);
        return imageEntity;
    }

    public ImageEntity getImageWithImageId(Long imageId) {
        if (imageId == null) return null;
        return imageRepository.findById(imageId).orElse(null); // todo should convert to a response object
    }



    private ImageEntity mapToEntity(Image image, UploadRequestModel uploadRequestModel) {
        if (image == null) return null;
        if (image.getContent() == null || image.getContent().isEmpty()) return null;
        String label = Strings.isBlank(uploadRequestModel.getLabel()) ? UUID.randomUUID().toString() : uploadRequestModel.getLabel();

        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setLabel(label);
        imageEntity.setImageBytes(image.getContent().toByteArray());

        return imageEntity;
    }

    private void saveImage(ImageEntity imageEntity) {
        if (imageEntity == null) return;
        imageRepository.save(imageEntity);
    }
}
