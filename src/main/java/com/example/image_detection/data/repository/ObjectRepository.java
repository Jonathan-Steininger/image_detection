package com.example.image_detection.data.repository;

import com.example.image_detection.data.entity.ObjectEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ObjectRepository extends CrudRepository<ObjectEntity,Long> {
    List<ObjectEntity> findDistinctByNameIn(String[] names); // todo I know I can pass a list, can I do an array
}
