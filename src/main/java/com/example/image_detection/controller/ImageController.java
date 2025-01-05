package com.example.image_detection.controller;

import com.example.image_detection.data.entity.ImageEntity;
import com.example.image_detection.data.model.ImageUploadResponse;
import com.example.image_detection.data.model.UploadRequest;
import com.example.image_detection.data.repository.ImageRepository;
import com.example.image_detection.service.DetectLabelsGcs;
import com.example.image_detection.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.google.cloud.vision.v1.Image;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageRepository imageRepository;
    private final ImageService imageService;

    // uploads image
    @PostMapping("/upload/image")
    public ResponseEntity<ImageUploadResponse> uploadImage(@RequestParam("image") MultipartFile file, @RequestParam("mybody") UploadRequest mybody)
            throws IOException {

        ImageEntity image = ImageEntity.builder()
                .label(file.getOriginalFilename())
//                .type(file.getContentType())
                .imageBytes(file.getBytes()).build();
        System.out.println("label thing " + mybody.getLabel());
        System.out.println("other thing " + mybody.getEnableObjectDetection());

//        imageRepository.save(image);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ImageUploadResponse("Image uploaded successfully: " +
                        file.getOriginalFilename()));
    }

    @PostMapping("/upload/image1")
    public ResponseEntity<ImageUploadResponse> uploadImage1()
            throws IOException {

        DetectLabelsGcs.detectLabelsGcs();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ImageUploadResponse("Image uploaded successfully: " +
                        "garbage test"));
    }

    // uploads image
    @PostMapping("/upload/image2")
    public ResponseEntity<ImageEntity> uploadImage2(@RequestBody UploadRequest uploadRequest)
            throws IOException {

        ImageEntity imageEntity = imageService.processImageUploadRequest(uploadRequest);
        if (imageEntity == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(imageEntity);
    }

    @PostMapping("/upload/image3")
    public ResponseEntity<ImageUploadResponse> uploadImage3(@RequestBody UploadRequest uploadRequest)
            throws IOException {

        Image image = DetectLabelsGcs.detectLabelsGcs3(uploadRequest.getImage());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ImageUploadResponse("Image uploaded successfully: " +
                "garbage test"));
    }




    // kinda returns metadata
    @GetMapping(value ="/get/image/info/{name}",
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ImageEntity getImageDetails(@PathVariable("name") String name) throws IOException {

        final Optional<ImageEntity> dbImage = imageRepository.findByLabel(name);

        ImageEntity image = ImageEntity.builder()
                .label(dbImage.get().getLabel())
                .imageBytes(dbImage.get().getImageBytes()).build();

        return image;
    }

    // returns actual image
    @GetMapping(value = "/get/image/{name}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("name") String name) throws IOException {

        final Optional<ImageEntity> dbImage = imageRepository.findByLabel(name);

        return ResponseEntity
                .ok()
//                .contentType(MediaType.valueOf("multipart/form-data;boundary=something"))
                .body(dbImage.get().getImageBytes());
    }
}
