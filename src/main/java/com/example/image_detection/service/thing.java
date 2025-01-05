package com.example.image_detection.service;

import com.example.image_detection.data.entity.TestThing1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class thing {

    private final TestThing1Service testThing1Service;

    @Scheduled(cron = "*/10 * * * * *")
    public void idk() {
        log.info("idk");
        TestThing1 testThing1 = testThing1Service.getTestThing1Name();

        log.info(testThing1.toString());

        TestThing1 testThing1Too = testThing1Service.getTestThing1();

        log.info(testThing1Too.toString());
    }
}
