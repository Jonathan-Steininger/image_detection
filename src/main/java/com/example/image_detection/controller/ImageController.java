package com.example.image_detection.controller;

import com.example.image_detection.data.entity.ImageEntity;
import com.example.image_detection.data.entity.ImageObjectsViewEntity;
import com.example.image_detection.data.model.ImageUploadResponse;
import com.example.image_detection.data.model.UploadRequest;
import com.example.image_detection.data.repository.ImageRepository;
import com.example.image_detection.service.DetectLabelsGcs;
import com.example.image_detection.service.ImageObjectsViewService;
import com.example.image_detection.service.ImageService;
import com.example.image_detection.service.ProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.google.cloud.vision.v1.Image;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final ProcessingService processingService;
    private final ImageObjectsViewService imageObjectsViewService;

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

    // uploads image GOOD ONE
    // todo rename method
    @PostMapping("/images")
    public ResponseEntity<ImageObjectsViewEntity> uploadImage2(@RequestBody UploadRequest uploadRequest) {

        ImageObjectsViewEntity imageObjectsViewEntity = processingService.processImageUploadRequest(uploadRequest);
        if (imageObjectsViewEntity == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(imageObjectsViewEntity);
    }

    // kinda returns metadata
    @GetMapping(value ="/images/{imageId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageObjectsViewEntity> getImage(@PathVariable("imageId") Long imageId) {

        ImageObjectsViewEntity imageObjectsViewEntity = processingService.processGetImageWithImageIdRequest(imageId);
        if (imageObjectsViewEntity == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // todo shouldn't neccessarily return a 500 error here
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(imageObjectsViewEntity);
    }

    // kinda returns metadata
    @GetMapping(value ="/images",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ImageObjectsViewEntity>> getImageQuery(@RequestParam(name = "objects", required = false) String objects) {

        List<ImageObjectsViewEntity> imageObjectsViewEntities = processingService.processGetImagesRequest(objects);
        if (CollectionUtils.isEmpty(imageObjectsViewEntities)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // todo shouldn't neccessarily return a 500 error here
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(imageObjectsViewEntities);
    }

//    // TODO HAVENT STARTED
//    @GetMapping(value ="/images",
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ImageEntity getImageQuery() {
//
//        return null;
//    }

    @PostMapping("/upload/image3")
    public ResponseEntity<ImageUploadResponse> uploadImage3(@RequestBody UploadRequest uploadRequest)
            throws IOException {

        Image image = DetectLabelsGcs.detectLabelsGcs3(uploadRequest.getImage());

        ImageEntity imageEntity = ImageEntity.builder()
                .label(uploadRequest.getLabel())
//                .type(file.getContentType())
                .imageBytes(image.getContent().toByteArray()).build(); // todo null and empty check
        System.out.println("label thing " + uploadRequest.getLabel());
        System.out.println("other thing " + uploadRequest.getEnableObjectDetection());

        imageRepository.save(imageEntity);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ImageUploadResponse("Image uploaded successfully: " +
                        uploadRequest.getLabel()));
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
    public ResponseEntity<byte[]> getImage2(@PathVariable("name") String name) throws IOException {

        final Optional<ImageEntity> dbImage = imageRepository.findByLabel(name);

        return ResponseEntity
                .ok()
//                .contentType(MediaType.valueOf("multipart/form-data;boundary=something"))
                .body(dbImage.get().getImageBytes());
    }

//    // returns actual image
//    @GetMapping(value = "/get/images",
//            produces = MediaType.IMAGE_JPEG_VALUE)
//    public ResponseEntity<List<byte[]>> getImages() throws IOException {
//
//        final List<ImageEntity> dbImage = imageRepository.findAllByOrderByIdDesc();
//        List<byte[]> images = new ArrayList<>();
//        for (ImageEntity imageEntity : dbImage) {
//            if (imageEntity.getImageBytes() != null && imageEntity.getImageBytes().length > 0) {
//                images.add(imageEntity.getImageBytes());
//            }
//        }
//
//        return ResponseEntity
//                .ok()
////                .contentType(MediaType.valueOf("multipart/form-data;boundary=something"))
//                .body(images);
//    }
}
