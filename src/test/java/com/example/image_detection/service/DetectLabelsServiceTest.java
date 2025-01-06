package com.example.image_detection.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DetectLabelsServiceTest {

    private DetectLabelsService detectLabelsService;

    @BeforeEach
    void setUp() {
        detectLabelsService = new DetectLabelsService();
    }

    @Test
    @DisplayName("When the enableObjectDetection field is missing, it should return null")
    void detectLabelsWhenNullObjectDetection() throws IOException {
        List<String> result = detectLabelsService.detectLabels(null, null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("When the enableObjectDetection field is false, it should return null")
    void detectLabelsWhenFalseObjectDetection() throws IOException {
        List<String> result = detectLabelsService.detectLabels(null, false);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("When the enableObjectDetection field is true and image is null, it should return null")
    void detectLabelsWhenImageNull() throws IOException {
        List<String> result = detectLabelsService.detectLabels(null, true);

        assertThat(result).isNull();
    }
}