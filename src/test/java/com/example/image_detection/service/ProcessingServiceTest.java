package com.example.image_detection.service;

import com.example.image_detection.data.entity.ImageEntity;
import com.example.image_detection.data.entity.ImageObjectsViewEntity;
import com.example.image_detection.data.model.ResponseModel;
import com.example.image_detection.data.model.UploadRequestModel;
import com.example.image_detection.util.ImageUtility;
import com.google.cloud.vision.v1.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessingServiceTest {
    @Mock
    private DetectLabelsService detectLabelsService;
    @Mock
    private ImageObjectsViewService imageObjectsViewService;
    @Mock
    private ImageService imageService;
    @Mock
    private ImageUtility imageUtility;
    @Mock
    private ObjectService objectService;

    private ProcessingService processingService;

    private List<ImageObjectsViewEntity> imageObjectsViewEntities;
    private UploadRequestModel uploadRequestModel;

    @BeforeEach
    void setUp() {
        processingService = new ProcessingService(detectLabelsService, imageObjectsViewService, imageService, imageUtility, objectService);

        ImageObjectsViewEntity iove1 = new ImageObjectsViewEntity();
        iove1.setLabel("label1");
        iove1.setId(1L);

        ImageObjectsViewEntity iove2 = new ImageObjectsViewEntity();
        iove2.setLabel("label2");
        iove2.setId(2L);

        ImageObjectsViewEntity iove3 = new ImageObjectsViewEntity();
        iove3.setLabel("label3");
        iove3.setId(3L);

        imageObjectsViewEntities = List.of(iove1, iove2, iove3);

        uploadRequestModel = new UploadRequestModel();
        uploadRequestModel.setLabel("label");
        uploadRequestModel.setImage("src/main/resources/images/image.jpg");
        uploadRequestModel.setEnableObjectDetection(true);
    }

    @Test
    @DisplayName("When the uploadRequestModel is null, it should return a bad request saying that the request is null")
    void processImageUploadRequestWhenUploadRequestModelIsNull() {
        ResponseEntity<ResponseModel> result = processingService.processImageUploadRequest(null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getMessage()).isEqualTo("Upload request is null"); // todo put into constants
    }

    @Test
    @DisplayName("When the uploadRequestModel image is null, it should return a bad request saying that the image field is blank")
    void processImageUploadRequestWhenImageFieldIsNull() {
        uploadRequestModel.setImage(null);

        ResponseEntity<ResponseModel> result = processingService.processImageUploadRequest(uploadRequestModel);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getMessage()).isEqualTo("Image field is blank");
    }

    @Test
    @DisplayName("When the uploadRequestModel contains necessary data, it should return a 200 saying Successfully uploaded image")
    void processImageUploadRequestWhenContainsNecessaryData() {
        ResponseEntity<ResponseModel> result = processingService.processImageUploadRequest(uploadRequestModel);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getMessage()).isEqualTo("Successfully uploaded image");
    }

    @Test
    @DisplayName("When mocks return values, it should return a 200 saying Successfully uploaded image")
    void processImageUploadRequestWhenContainsNecessaryData2() throws IOException {
        when(imageUtility.getImageFromSource(uploadRequestModel.getImage())).thenReturn(Image.newBuilder().build());
        when(imageService.upload(any(Image.class), any(UploadRequestModel.class))).thenReturn(new ImageEntity());
        when(detectLabelsService.detectLabels(any(Image.class), any(Boolean.class))).thenReturn(new ArrayList<>());
        when(imageObjectsViewService.getImageObjectsViewEntityForImage(any(ImageEntity.class))).thenReturn(new ImageObjectsViewEntity());

        ResponseEntity<ResponseModel> result = processingService.processImageUploadRequest(uploadRequestModel);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getMessage()).isEqualTo("Successfully uploaded image");
    }

    @Test
    @DisplayName("When the an exception is thrown, it should return a 500")
    void processImageUploadRequestWhenThrowsAnException() throws IOException {
        when(imageUtility.getImageFromSource(uploadRequestModel.getImage())).thenThrow(new IOException());

        ResponseEntity<ResponseModel> result = processingService.processImageUploadRequest(uploadRequestModel);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(result.getBody()).isNotNull();
    }

    @Test
    @DisplayName("When the imageId is null, it should return bad request saying that the imageId is null")
    void processGetImageWithImageIdRequestWhenImageIdIsNull() {
        ResponseEntity<ResponseModel> result = processingService.processGetImageWithImageIdRequest(null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getMessage()).isEqualTo("imageId is null");
    }

    @Test
    @DisplayName("When the imageId is provided, but not found in the db, it should return 200 saying that the image for that imageId was not found")
    void processGetImageWithImageIdRequestWhenImageIdIsProvidedButNotFound() {
        ResponseEntity<ResponseModel> result = processingService.processGetImageWithImageIdRequest(1L);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getMessage()).isEqualTo("Image with imageId 1 not found");
        assertThat(result.getBody().getImageObjects()).isNull();
    }

    @Test
    @DisplayName("When the imageId is provided, and is found in the db, it should return 200 saying that the image for that imageId was found")
    void processGetImageWithImageIdRequestWhenImageIdIsProvidedAndIsFound() {
        when(imageObjectsViewService.getImageObjectsViewEntityForImageId(1L)).thenReturn(new ImageObjectsViewEntity());

        ResponseEntity<ResponseModel> result = processingService.processGetImageWithImageIdRequest(1L);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getMessage()).isEqualTo("Image with imageId 1 found");
        assertThat(result.getBody().getImageObjects()).isNotNull();
        assertThat(result.getBody().getImageObjects().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("When an exception is thrown, it should return a 500 and the imageObjects array should be null")
    void processGetImageWithImageIdRequestWhenImageIdIsProvidedAndIsFound2() {
        when(imageObjectsViewService.getImageObjectsViewEntityForImageId(1L)).thenThrow(new RuntimeException());

        ResponseEntity<ResponseModel> result = processingService.processGetImageWithImageIdRequest(1L);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getImageObjects()).isNull();
    }

    @Test
    @DisplayName("When the imageId is null, it should return bad request")
    void processGetImageViewWithImageIdRequestWhenImageIdIsNull() {
        ResponseEntity<byte[]> result = processingService.processGetImageViewWithImageIdRequest(null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("When the imageId is not null, but the id is not found, it should return a 200 but the body should be null")
    void processGetImageViewWithImageIdRequestWhenImageIdIsNotNullButIdIsNotFound() {
        when(imageService.getImageWithImageId(1L)).thenReturn(null);

        ResponseEntity<byte[]> result = processingService.processGetImageViewWithImageIdRequest(1L);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNull();
    }

    @Test
    @DisplayName("When the imageId is not null, and the id is found, it should return a 200 but the body should not be null")
    void processGetImageViewWithImageIdRequestWhenImageIdIsNotNullAndIdIsFound() {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setImageBytes(new byte[]{});
        when(imageService.getImageWithImageId(1L)).thenReturn(imageEntity);

        ResponseEntity<byte[]> result = processingService.processGetImageViewWithImageIdRequest(1L);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
    }

    @Test
    @DisplayName("When an exception is thrown, it should return a 500 and the body should be null")
    void processGetImageViewWithImageIdRequestWhenExceptionIsThrown() {
        when(imageService.getImageWithImageId(1L)).thenThrow(new RuntimeException());

        ResponseEntity<byte[]> result = processingService.processGetImageViewWithImageIdRequest(1L);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(result.getBody()).isNull();
    }

    @Test
    @DisplayName("When the objectsString is null, it should return a 200 and all values in db saying Successfully retrieved matching images")
    void processGetImagesRequestWhenObjectsStringIsNull() {
        when(imageObjectsViewService.getAllImageObjectsViews()).thenReturn(imageObjectsViewEntities);
        ResponseEntity<ResponseModel> result = processingService.processGetImagesRequest(null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getMessage()).isEqualTo("Successfully retrieved matching images");
        assertThat(result.getBody().getImageObjects()).isNotNull();
        assertThat(result.getBody().getImageObjects().size()).isEqualTo(3);
        verify(objectService, times(0)).getImageIdsForGivenObjectNames(any(String.class));
    }

    @Test
    @DisplayName("When the objectsString is not null, but no values are found in the db, it should return a 200 saying No images found for search criteria")
    void processGetImagesRequestWhenObjectsStringIsNotNullButNoValuesAreFound() {
        List<Long> ids = List.of(1L, 2L, 3L);
        when(objectService.getImageIdsForGivenObjectNames(any(String.class))).thenReturn(ids);
        when(imageObjectsViewService.getAllImageObjectsViewsForIds(ids)).thenReturn(null);
        ResponseEntity<ResponseModel> result = processingService.processGetImagesRequest("1,2,3");

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getMessage()).isEqualTo("No images found for search criteria");
        assertThat(result.getBody().getImageObjects()).isNull();
        verify(imageObjectsViewService, times(0)).getAllImageObjectsViews();
    }

    @Test
    @DisplayName("When the objectsString is not null, but no values are found in the db, it should return a 200 saying No images found for search criteria")
    void processGetImagesRequestWhenObjectsStringIsNotNullButNoValuesAreFound2() {
        List<Long> ids = List.of(1L, 2L, 3L);
        when(objectService.getImageIdsForGivenObjectNames(any(String.class))).thenReturn(ids);
        when(imageObjectsViewService.getAllImageObjectsViewsForIds(ids)).thenReturn(imageObjectsViewEntities);
        ResponseEntity<ResponseModel> result = processingService.processGetImagesRequest("1,2,3");

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getMessage()).isEqualTo("Successfully retrieved matching images");
        assertThat(result.getBody().getImageObjects()).isNotNull();
        assertThat(result.getBody().getImageObjects().size()).isEqualTo(3);
        verify(imageObjectsViewService, times(0)).getAllImageObjectsViews();
    }

    @Test
    @DisplayName("When an exception is thrown, it should return a 500 and the image objects should be null")
    void processGetImagesRequestWhenAnExceptionIsThrown() {
        when(imageObjectsViewService.getAllImageObjectsViews()).thenThrow(new RuntimeException());
        ResponseEntity<ResponseModel> result = processingService.processGetImagesRequest(null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getImageObjects()).isNull();
    }
}