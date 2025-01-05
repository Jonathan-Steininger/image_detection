package com.example.image_detection.service;

import com.example.image_detection.data.entity.TestThing1;
import com.example.image_detection.data.repository.TestThing1Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestThing1Service {
    private final TestThing1Repository testThing1Repository;

    public TestThing1 getTestThing1() {
        return testThing1Repository.findById(1).orElse(new TestThing1());
    }

    public TestThing1 getTestThing1Name() {
        return testThing1Repository.findByName("first");
    }
}
