package com.example.image_detection.data.repository;

import com.example.image_detection.data.entity.ImageEntity;
import org.springframework.data.repository.CrudRepository;


public interface ImageRepository extends CrudRepository<ImageEntity, Long> {
}