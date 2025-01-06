package com.example.image_detection.service;

import com.example.image_detection.data.entity.ImageEntity;
import com.example.image_detection.data.model.UploadRequestModel;
import com.example.image_detection.data.repository.ImageRepository;
import com.google.cloud.vision.v1.Image;
import com.google.protobuf.ByteString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {
    @Mock
    private ImageRepository imageRepository;

    private ImageService imageService;

    private UploadRequestModel uploadRequestModel;

    @BeforeEach
    void setUp() {
        imageService = new ImageService(imageRepository);

        uploadRequestModel = new UploadRequestModel();
        uploadRequestModel.setLabel("label");
    }

    @Test
    @DisplayName("When the uploadRequestModel label is null, it should return an ImageEntity with a random UUID for the label")
    void uploadWithNullLabel() {
        uploadRequestModel.setLabel(null);

        ImageEntity result = imageService.upload(null, uploadRequestModel);

        assertThat(result).isNotNull();
        assertThat(result.getLabel()).isNotNull();
        assertThat(result.getLabel()).isNotEqualTo(uploadRequestModel.getLabel());
    }

    @Test
    @DisplayName("When the uploadRequestModel label is not null, it should return an ImageEntity with that value for the label")
    void uploadWithLabel() {
        ImageEntity result = imageService.upload(null, uploadRequestModel);

        assertThat(result).isNotNull();
        assertThat(result.getLabel()).isNotNull();
        assertThat(result.getLabel()).isEqualTo(uploadRequestModel.getLabel());
    }

    @Test
    @DisplayName("When the image is null, it should return a null value for the imageBytes")
    void uploadWithNullImage() {
        ImageEntity result = imageService.upload(null, uploadRequestModel);

        assertThat(result).isNotNull();
        assertThat(result.getImageBytes()).isNull();
    }

    @Test
    @DisplayName("When the image is not null, but the content is null, it should return a null value for the imageBytes")
    void uploadWithNullImageContent() {
        Image image = Image.newBuilder().build();

        ImageEntity result = imageService.upload(image, uploadRequestModel);

        assertThat(result).isNotNull();
        assertThat(result.getImageBytes()).isNull();
    }

    @Test
    @DisplayName("When the image is not null, and the content is not null, it should return a non-null value for the imageBytes")
    void uploadWithImageContent() {
        Image image = Image.newBuilder().setContent(ByteString.copyFrom("bytes".getBytes())).build();

        ImageEntity result = imageService.upload(image, uploadRequestModel);

        assertThat(result).isNotNull();
        assertThat(result.getImageBytes()).isNotNull();
    }

    @Test
    @DisplayName("When the imageId is null, it should return null")
    void getImageWithImageIdWhenNull() {
        ImageEntity result = imageService.getImageWithImageId(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("When the imageId is not null, it should return a value")
    void getImageWithImageIdWhenNotNull() {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setId(1L);
        imageEntity.setLabel("label");
        when(imageRepository.findById(1L)).thenReturn(Optional.of(imageEntity));

        ImageEntity result = imageService.getImageWithImageId(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getLabel()).isEqualTo("label");
    }
}