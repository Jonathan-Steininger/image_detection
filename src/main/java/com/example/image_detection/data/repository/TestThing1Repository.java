package com.example.image_detection.data.repository;

import com.example.image_detection.data.entity.TestThing1;
import org.springframework.data.repository.CrudRepository;

public interface TestThing1Repository extends CrudRepository<TestThing1, Integer> {
    TestThing1 findByName(String name);

}
