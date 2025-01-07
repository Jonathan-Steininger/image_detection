package com.example.image_detection.service;

import com.example.image_detection.data.entity.ImageEntity;
import com.example.image_detection.data.entity.ImageObjectsViewEntity;
import com.example.image_detection.data.repository.ImageObjectsViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageObjectsViewService {
    private final ImageObjectsViewRepository imageObjectsViewRepository;

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

    public ImageObjectsViewEntity mapImageObjectsViewEntity(ImageEntity imageEntity, List<String> objects) {
        if (imageEntity == null) return null;

        ImageObjectsViewEntity imageObjectsViewEntity = new ImageObjectsViewEntity();
        imageObjectsViewEntity.setId(imageEntity.getId());
        imageObjectsViewEntity.setLabel(imageEntity.getLabel());
        if (!CollectionUtils.isEmpty(objects)) {
            imageObjectsViewEntity.setObjects(objects.toArray(new String[0]));
        }
        return imageObjectsViewEntity;
    }
}
