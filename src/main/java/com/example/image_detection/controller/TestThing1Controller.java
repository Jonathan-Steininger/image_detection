package com.example.image_detection.controller;

import com.example.image_detection.data.entity.ImageEntity;
import com.example.image_detection.data.entity.TestThing1;
import com.example.image_detection.data.repository.ImageRepository;
import com.example.image_detection.service.DetectLabelsGcs;
import com.example.image_detection.service.TestThing1Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestThing1Controller {

    private final TestThing1Service testThing1Service;
    private final ImageRepository imageRepository;

    @GetMapping("/testThing1")
    public TestThing1 testThing1() throws IOException {
        DetectLabelsGcs.detectLabelsGcs();
        return testThing1Service.getTestThing1();
    }


//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Produces("image/jpg")
    // performs detection
    @PostMapping("/testThing2")
    public TestThing1 testThing2() throws IOException {
        final Optional<ImageEntity> dbImage = imageRepository.findByLabel("dog_cat_pic.jpg");

        ImageEntity image = ImageEntity.builder()
                .label(dbImage.get().getLabel())
                .imageBytes(dbImage.get().getImageBytes()).build();

        DetectLabelsGcs.detectLabelsGcs2(image);
        return testThing1Service.getTestThing1();
    }
}
