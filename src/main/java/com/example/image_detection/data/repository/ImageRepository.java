package com.example.image_detection.data.repository;

import com.example.image_detection.data.entity.ImageEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends CrudRepository<ImageEntity,Long> {
    Optional<ImageEntity> findByLabel(String label);
    List<ImageEntity> findAllByOrderByIdDesc();

}