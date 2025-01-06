package com.example.image_detection.util;

import com.google.cloud.vision.v1.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ImageUtilityTest {

    private ImageUtility imageUtility;

    @BeforeEach
    void setUp() {
        imageUtility = new ImageUtility();
    }

    @Test
    @DisplayName("When the path is null, it should return null")
    void getImageFromSourceWhenNull() throws IOException {
        Image result = imageUtility.getImageFromSource(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("When the path is empty, it should return null")
    void getImageFromSourceWhenEmpty() throws IOException {
        Image result = imageUtility.getImageFromSource("");

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("When the path is blank, it should return null")
    void getImageFromSourceWhenBlank() throws IOException {
        Image result = imageUtility.getImageFromSource(" ");

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("When the path starts with 'http', it should return an image with the source value set")
    void getImageFromSourceWhenStartsWithHttp() throws IOException {
        Image result = imageUtility.getImageFromSource("http://google.com/image.jpg");

        assertThat(result).isNotNull();
        assertThat(result.getSource()).isNotNull();
        assertThat(result.getSource().getImageUri()).isNotNull();
        assertThat(result.getSource().getImageUri()).isEqualTo("http://google.com/image.jpg");
    }

    @Test
    @DisplayName("When the path does not start with 'http', it should return an image with the content value set")
    void getImageFromSourceWhenNotStartsWithHttp() throws IOException {
        Image result = imageUtility.getImageFromSource("src/main/resources/images/image.jpg");

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isNotNull();
    }
}