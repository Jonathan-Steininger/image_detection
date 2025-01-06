package com.example.image_detection.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.annotation.Nullable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UploadRequestModel {
    @Nullable
    private String label;
    private String image;
    @Nullable
    private Boolean enableObjectDetection;
}
