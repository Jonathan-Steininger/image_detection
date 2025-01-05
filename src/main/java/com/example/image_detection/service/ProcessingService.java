package com.example.image_detection.service;

import com.example.image_detection.data.entity.ImageEntity;
import com.example.image_detection.data.entity.ImageObjectsViewEntity;
import com.example.image_detection.data.model.UploadRequest;
import com.example.image_detection.data.repository.ObjectRepository;
import com.example.image_detection.util.ImageUtility;
import com.google.cloud.vision.v1.Image;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessingService {
    private final ImageService imageService;
    private final ObjectService objectService;
    private final ImageObjectsViewService imageObjectsViewService;
    private final ObjectRepository objectRepository;

    public ImageObjectsViewEntity processImageUploadRequest(UploadRequest uploadRequest) {
        if (uploadRequest == null) return null;
        if (Strings.isBlank(uploadRequest.getImage())) return null;

        Image image = ImageUtility.getImageFromSource(uploadRequest.getImage());
        ImageEntity imageEntity = imageService.upload(image, uploadRequest);
        try {
            List<String> objects = DetectLabelsGcs.detectLabels(image, uploadRequest.getEnableObjectDetection());
            objectService.saveObjectsForImage(objects, imageEntity);
        } catch (IOException e) {
            log.error(e.getMessage()); // todo handle better
        }

        return imageObjectsViewService.getImageObjectsViewEntityForImage(imageEntity);
    }

    public ImageObjectsViewEntity processGetImageWithImageIdRequest(Long imageId) {
        if (imageId == null) return null;
        return imageObjectsViewService.getImageObjectsViewEntityForImageId(imageId); // todo should convert to a response object
    }

    public List<ImageObjectsViewEntity> processGetImagesRequest(String objectsString) {
        if (Strings.isBlank(objectsString)) return imageObjectsViewService.getAllImageObjectsViews();
        List<Long> ids = objectService.getImageIdsForGivenObjectNames(objectsString);

        return imageObjectsViewService.getAllImageObjectsViewsForIds(ids); // todo should convert to a response object
    }
}
