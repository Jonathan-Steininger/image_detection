package com.example.image_detection.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class thing {


    // todo DELETE ME
    @Scheduled(cron = "*/10 * * * * *")
    public void idk() {
        log.info("idk");

    }
}
