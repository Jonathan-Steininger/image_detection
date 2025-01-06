package com.example.image_detection.data.repository;

import com.example.image_detection.data.entity.ObjectEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ObjectRepository extends CrudRepository<ObjectEntity, Long> {
    List<ObjectEntity> findDistinctByNameIn(String[] names);
}
