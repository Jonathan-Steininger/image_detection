package com.example.image_detection.service;

import com.example.image_detection.data.entity.ImageEntity;
import com.example.image_detection.data.entity.ObjectEntity;
import com.example.image_detection.data.repository.ObjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObjectServiceTest {
    @Mock
    private ObjectRepository objectRepository;

    private ObjectService objectService;

    private ImageEntity imageEntity;
    private List<String> objects;

    @BeforeEach
    void setUp() {
        objectService = new ObjectService(objectRepository);

        imageEntity = new ImageEntity();
        imageEntity.setId(1L);
        imageEntity.setLabel("label");

        objects = List.of("object1", "object2", "object3");
    }

    @Test
    @DisplayName("When the list of objects is null, it should return without processing")
    void saveObjectsForImageWhenObjectListIsNull() {
        objectService.saveObjectsForImage(null, imageEntity);

        verify(objectRepository, times(0)).save(any(ObjectEntity.class));
    }

    @Test
    @DisplayName("When the list of objects is empty, it should return without processing")
    void saveObjectsForImageWhenObjectListIsEmpty() {
        objectService.saveObjectsForImage(Collections.emptyList(), imageEntity);

        verify(objectRepository, times(0)).save(any(ObjectEntity.class));
    }

    @Test
    @DisplayName("When the list of objects is present, but the imageEntity is null, it should return without processing")
    void saveObjectsForImageWhenImageEntityIsNull() {
        objectService.saveObjectsForImage(objects, null);

        verify(objectRepository, times(0)).save(any(ObjectEntity.class));
    }

    @Test
    @DisplayName("When the list of objects is present, but the imageEntity id is null, it should return without processing")
    void saveObjectsForImageWhenImageEntityIdIsNull() {
        imageEntity.setId(null);
        objectService.saveObjectsForImage(objects, imageEntity);

        verify(objectRepository, times(0)).save(any(ObjectEntity.class));
    }

    @Test
    @DisplayName("When the list of objects has 3 values, and the imageEntity id is present, it should return after calling the save method 3 times")
    void saveObjectsForImageWhenDataValid() {
        objectService.saveObjectsForImage(objects, imageEntity);

        verify(objectRepository, times(3)).save(any(ObjectEntity.class));
    }

    @Test
    @DisplayName("When the nameString is null, it should return null")
    void getImageIdsForGivenObjectNamesWhenNull() {
        List<Long> result = objectService.getImageIdsForGivenObjectNames(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("When the nameString is empty, it should return null")
    void getImageIdsForGivenObjectNamesWhenEmpty() {
        List<Long> result = objectService.getImageIdsForGivenObjectNames("");

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("When the nameString is blank, it should return null")
    void getImageIdsForGivenObjectNamesWhenBlank() {
        List<Long> result = objectService.getImageIdsForGivenObjectNames("   ");

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("When the nameString is has multiple blank values, it should return null")
    void getImageIdsForGivenObjectNamesWhenHasMultipleBlankValues() {
        List<Long> result = objectService.getImageIdsForGivenObjectNames(" ,  ");

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("When the nameString has a value, it should return a value")
    void getImageIdsForGivenObjectNamesWhenHasAValue() {
        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setImageId(1L);
        List<ObjectEntity> objectEntities = List.of(objectEntity);
        String[] objectNames = {"object1"};
        when(objectRepository.findDistinctByNameIn(objectNames)).thenReturn(objectEntities);

        List<Long> result = objectService.getImageIdsForGivenObjectNames("object1");

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(1L);
    }

    @Test
    @DisplayName("When the nameString has a value, it can return multiple values")
    void getImageIdsForGivenObjectNamesWhenHasAValueReturnsMultipleValues() {
        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setImageId(1L);
        ObjectEntity objectEntity2 = new ObjectEntity();
        objectEntity2.setImageId(2L);

        List<ObjectEntity> objectEntities = List.of(objectEntity, objectEntity2);
        String[] objectNames = {"object1"};
        when(objectRepository.findDistinctByNameIn(objectNames)).thenReturn(objectEntities);

        List<Long> result = objectService.getImageIdsForGivenObjectNames("object1");

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualTo(1L);
        assertThat(result.get(1)).isEqualTo(2L);
    }
}