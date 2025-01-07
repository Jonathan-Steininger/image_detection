package com.example.image_detection.data.model;

import com.example.image_detection.data.entity.ImageObjectsViewEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ResponseModel {
    private String message;
    private List<ImageObjectsViewEntity> imageObjects;
}
