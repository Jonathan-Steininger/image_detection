package com.example.image_detection.controller;

import com.example.image_detection.data.entity.ImageEntity;
import com.example.image_detection.data.model.ResponseModel;
import com.example.image_detection.data.model.UploadRequestModel;
import com.example.image_detection.data.repository.ImageRepository;
import com.example.image_detection.service.ProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageRepository imageRepository; // todo delete me
    private final ProcessingService processingService;

    // uploads image
    @PostMapping("/images")
    public ResponseEntity<ResponseModel> uploadImage(@RequestBody UploadRequestModel uploadRequestModel) {
        return processingService.processImageUploadRequest(uploadRequestModel);
    }

    // kinda returns metadata
    @GetMapping(value = "/images/{imageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel> getImage(@PathVariable("imageId") Long imageId) {
        return processingService.processGetImageWithImageIdRequest(imageId);
    }

    // kinda returns metadata
    @GetMapping(value = "/images", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel> getImageQuery(@RequestParam(name = "objects", required = false) String objects) {
        return processingService.processGetImagesRequest(objects);
    }

    // returns actual image
    @GetMapping(value = "/images/view/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageView(@PathVariable("imageId") Long imageId) {
        return processingService.processGetImageViewWithImageIdRequest(imageId);
    }
}
