package com.example.image_detection.data.repository;

import com.example.image_detection.data.entity.ImageObjectsViewEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ImageObjectsViewRepository extends CrudRepository<ImageObjectsViewEntity, Long> {
    List<ImageObjectsViewEntity> findAllByIdIn(List<Long> ids);
    List<ImageObjectsViewEntity> findAll();
}
