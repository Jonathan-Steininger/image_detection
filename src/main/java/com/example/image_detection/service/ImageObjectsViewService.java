package com.example.image_detection.service;

import com.example.image_detection.data.entity.ImageEntity;
import com.example.image_detection.data.entity.ImageObjectsViewEntity;
import com.example.image_detection.data.repository.ImageObjectsViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageObjectsViewService {
    private final ImageObjectsViewRepository imageObjectsViewRepository;

    public ImageObjectsViewEntity getImageObjectsViewEntityForImage(ImageEntity imageEntity) {
        if (imageEntity == null || imageEntity.getId() == null) return null;
        return imageObjectsViewRepository.findById(imageEntity.getId()).orElse(null);
    }

    public ImageObjectsViewEntity getImageObjectsViewEntityForImageId(Long imageId) {
        if (imageId == null) return null;
        return imageObjectsViewRepository.findById(imageId).orElse(null);
    }

    public List<ImageObjectsViewEntity> getAllImageObjectsViewsForIds(List<Long> imageIds) {
        if (CollectionUtils.isEmpty(imageIds)) return null;
        return imageObjectsViewRepository.findAllByIdIn(imageIds);
    }

    public List<ImageObjectsViewEntity> getAllImageObjectsViews() {
        return imageObjectsViewRepository.findAll();
    }
}
