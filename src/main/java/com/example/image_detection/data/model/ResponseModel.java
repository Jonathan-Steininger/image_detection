package com.example.image_detection.data.model;

import com.example.image_detection.data.entity.ImageObjectsViewEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseModel {
    private String message;
    private List<ImageObjectsViewEntity> imageObjects;
}
