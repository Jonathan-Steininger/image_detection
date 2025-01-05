package com.example.image_detection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // todo delete me
@SpringBootApplication
public class ImageDetectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageDetectionApplication.class, args);
	}

}
