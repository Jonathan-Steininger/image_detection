package com.example.image_detection.service;

import com.example.image_detection.data.entity.ImageEntity;
import com.example.image_detection.data.entity.ImageObjectsViewEntity;
import com.example.image_detection.data.model.ResponseModel;
import com.example.image_detection.data.model.UploadRequestModel;
import com.example.image_detection.util.ImageUtility;
import com.google.cloud.vision.v1.Image;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessingService {
    private final DetectLabelsService detectLabelsService;
    private final ImageObjectsViewService imageObjectsViewService;
    private final ImageService imageService;
    private final ImageUtility imageUtility;
    private final ObjectService objectService;

    public ResponseEntity<ResponseModel> processImageUploadRequest(UploadRequestModel uploadRequestModel) {
        if (uploadRequestModel == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ResponseModel.builder().message("Upload request is null").build());
        }
        if (Strings.isBlank(uploadRequestModel.getImage())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ResponseModel.builder().message("Image field is blank").build());
        }

        try {
            Image image = imageUtility.getImageFromSource(uploadRequestModel.getImage());
            List<String> detectedLabels = detectLabelsService.detectLabels(image, uploadRequestModel.getEnableObjectDetection());

            ImageEntity imageEntity = imageService.upload(image, uploadRequestModel);
            objectService.saveObjectsForImage(detectedLabels, imageEntity);
            ImageObjectsViewEntity imageObjectsViewEntity = imageObjectsViewService.mapImageObjectsViewEntity(imageEntity, detectedLabels);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ResponseModel
                            .builder()
                            .message("Successfully uploaded image")
                            .imageObjects(convertToList(imageObjectsViewEntity))
                            .build());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseModel
                            .builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    public ResponseEntity<ResponseModel> processGetImageWithImageIdRequest(Long imageId) {
        if (imageId == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ResponseModel.builder().message("imageId is null").build());
        }

        try {
            ImageObjectsViewEntity imageObjectsViewEntity = imageObjectsViewService.getImageObjectsViewEntityForImageId(imageId);

            String message;
            if (imageObjectsViewEntity == null) {
                message = "Image with imageId " + imageId + " not found";
            } else {
                message = "Image with imageId " + imageId + " found";
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ResponseModel.builder().message(message).imageObjects(convertToList(imageObjectsViewEntity)).build());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseModel.builder().message(e.getMessage()).build());
        }
    }

    public ResponseEntity<byte[]> processGetImageViewWithImageIdRequest(Long imageId) {
        if (imageId == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        try {
            ImageEntity image = imageService.getImageWithImageId(imageId);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(image != null ? image.getImageBytes() : null);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<ResponseModel> processGetImagesRequest(String objectsString) {
        try {
            List<ImageObjectsViewEntity> imageObjectsViewEntities;
            if (Strings.isBlank(objectsString)) {
                imageObjectsViewEntities = imageObjectsViewService.getAllImageObjectsViews();
            } else {
                List<Long> ids = objectService.getImageIdsForGivenObjectNames(objectsString);
                imageObjectsViewEntities = imageObjectsViewService.getAllImageObjectsViewsForIds(ids);
            }

            String message;
            if (CollectionUtils.isEmpty(imageObjectsViewEntities)) {
                message = "No images found for search criteria";
            } else {
                message = "Successfully retrieved matching images";
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ResponseModel.builder().message(message).imageObjects(imageObjectsViewEntities).build());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseModel.builder().message(e.getMessage()).build());
        }
    }

    private List<ImageObjectsViewEntity> convertToList(ImageObjectsViewEntity imageObjectsViewEntity) {
        return imageObjectsViewEntity == null ? null : List.of(imageObjectsViewEntity);
    }
}
