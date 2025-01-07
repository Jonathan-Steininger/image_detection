package com.example.image_detection.data.model;

import lombok.Data;

import javax.annotation.Nullable;

@Data
public class UploadRequestModel {
    @Nullable
    private String label;
    private String image;
    @Nullable
    private Boolean enableObjectDetection;
}
