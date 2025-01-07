package com.example.image_detection.service;

import com.example.image_detection.data.entity.ImageEntity;
import com.example.image_detection.data.entity.ImageObjectsViewEntity;
import com.example.image_detection.data.repository.ImageObjectsViewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageObjectsViewServiceTest {
    @Mock
    private ImageObjectsViewRepository imageObjectsViewRepository;

    private ImageObjectsViewService imageObjectsViewService;

    private ImageEntity imageEntity;
    private ImageObjectsViewEntity imageObjectsViewEntity;
    private List<ImageObjectsViewEntity> imageObjectsViewEntities;
    private List<String> objects;

    @BeforeEach
    void setUp() {
        imageObjectsViewService = new ImageObjectsViewService(imageObjectsViewRepository);

        imageEntity = new ImageEntity();
        imageEntity.setId(1L);
        imageEntity.setLabel("labelImage");

        imageObjectsViewEntity = new ImageObjectsViewEntity();
        imageObjectsViewEntity.setId(1L);
        imageObjectsViewEntity.setLabel("labelImageObjectsViewEntity");

        ImageObjectsViewEntity iove1 = new ImageObjectsViewEntity();
        iove1.setId(1L);
        ImageObjectsViewEntity iove2 = new ImageObjectsViewEntity();
        iove2.setId(2L);
        ImageObjectsViewEntity iove3 = new ImageObjectsViewEntity();
        iove3.setId(3L);
        imageObjectsViewEntities = Arrays.asList(iove1, iove2, iove3);

        objects = Arrays.asList("object1", "object2", "object3");
    }

    @Test
    @DisplayName("When getting the view for an image id, and the image id is null, it should return null")
    void getImageObjectsViewEntityForImageIdWhenIdIsNull() {
        ImageObjectsViewEntity result = imageObjectsViewService.getImageObjectsViewEntityForImageId(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("When getting the view for an image id, and the image id is present, it should return value")
    void getImageObjectsViewEntityForImageIdWhenIdIsPresent() {
        when(imageObjectsViewRepository.findById(1L)).thenReturn(Optional.of(imageObjectsViewEntity));

        ImageObjectsViewEntity result = imageObjectsViewService.getImageObjectsViewEntityForImageId(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getLabel()).isEqualTo("labelImageObjectsViewEntity");
    }

    @Test
    @DisplayName("When getting all views for a list of image ids, and the list is null, it should return null")
    void getAllImageObjectsViewsForIdsWhenNull() {
        List<ImageObjectsViewEntity> result = imageObjectsViewService.getAllImageObjectsViewsForIds(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("When getting all views for a list of image ids, and the list is empty, it should return null")
    void getAllImageObjectsViewsForIdsWhenEmpty() {
        List<ImageObjectsViewEntity> result = imageObjectsViewService.getAllImageObjectsViewsForIds(Collections.emptyList());

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("When getting all views for a list of image ids, and the list has values, it should return a list of values")
    void getAllImageObjectsViewsForIdsWhenHasValues() {
        List<Long> imageIds = Arrays.asList(1L, 2L, 3L);

        when(imageObjectsViewRepository.findAllByIdIn(imageIds)).thenReturn(imageObjectsViewEntities);

        List<ImageObjectsViewEntity> result = imageObjectsViewService.getAllImageObjectsViewsForIds(imageIds);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(2).getId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("When getting all views, it should return multiple values")
    void getAllImageObjectsViews() {
        when(imageObjectsViewRepository.findAll()).thenReturn(imageObjectsViewEntities);

        List<ImageObjectsViewEntity> result = imageObjectsViewService.getAllImageObjectsViews();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(2).getId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("When mapping the entity, and the imageEntity is null, it should return null")
    void mapImageObjectsViewEntityWhenNull() {
        ImageObjectsViewEntity result = imageObjectsViewService.mapImageObjectsViewEntity(null, objects);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("When mapping the entity, and the imageEntity is not null, but the objects list is null, it should return a value, but the objects array should be null")
    void mapImageObjectsViewEntityWhenNullObjects() {
        ImageObjectsViewEntity result = imageObjectsViewService.mapImageObjectsViewEntity(imageEntity, null);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getLabel()).isEqualTo("labelImage");
        assertThat(result.getObjects()).isNull();
    }

    @Test
    @DisplayName("When mapping the entity, and the imageEntity is not null, but the objects list is empty, it should return a value, but the objects array should be null")
    void mapImageObjectsViewEntityWhenEmptyObjects() {
        ImageObjectsViewEntity result = imageObjectsViewService.mapImageObjectsViewEntity(imageEntity, Collections.emptyList());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getLabel()).isEqualTo("labelImage");
        assertThat(result.getObjects()).isNull();
    }

    @Test
    @DisplayName("When mapping the entity, and the imageEntity is not null, and the objects list has 3 values, it should return a value, and the objects array should contain all 3 objects")
    void mapImageObjectsViewEntityWhenHas3Objects() {
        ImageObjectsViewEntity result = imageObjectsViewService.mapImageObjectsViewEntity(imageEntity, objects);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getLabel()).isEqualTo("labelImage");
        assertThat(result.getObjects()).isNotNull();
        assertThat(result.getObjects().length).isEqualTo(3);
        assertThat(result.getObjects()[0]).isEqualTo("object1");
        assertThat(result.getObjects()[1]).isEqualTo("object2");
        assertThat(result.getObjects()[2]).isEqualTo("object3");
    }
}