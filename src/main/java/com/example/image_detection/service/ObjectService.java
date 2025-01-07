package com.example.image_detection.service;

import com.example.image_detection.data.entity.ImageEntity;
import com.example.image_detection.data.entity.ObjectEntity;
import com.example.image_detection.data.repository.ObjectRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ObjectService {
    private final ObjectRepository objectRepository;

    @Transactional
    public void saveObjectsForImage(List<String> objects, ImageEntity imageEntity) {
        if (CollectionUtils.isEmpty(objects)) return;
        if (imageEntity == null || imageEntity.getId() == null) return;
        Long imageId = imageEntity.getId();

        for (String object : objects) {
            ObjectEntity objectEntity = mapToObjectEntity(object, imageId);
            objectRepository.save(objectEntity);
        }
    }

    public List<Long> getImageIdsForGivenObjectNames(String namesString) {
        if (Strings.isBlank(namesString)) return null;
        String[] names = sanitizeStringInput(namesString);

        List<ObjectEntity> objectEntities = objectRepository.findDistinctByNameIn(names);
        return getImageIdsFromObjectEntities(objectEntities);
    }

    private String[] sanitizeStringInput(String namesString) {
        String[] names = namesString.replace("\"","").split(",");
        return Arrays.stream(names).map(String::trim).map(String::toLowerCase).toArray(unused -> names);
    }

    private List<Long> getImageIdsFromObjectEntities(List<ObjectEntity> objectEntities) {
        if (CollectionUtils.isEmpty(objectEntities)) return null;
        List<Long> ids = new ArrayList<>();
        for (ObjectEntity objectEntity : objectEntities) {
            ids.add(objectEntity.getImageId());
        }
        return ids;
    }

    private ObjectEntity mapToObjectEntity(String object, Long imageId) {
        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setName(object);
        objectEntity.setImageId(imageId);
        return objectEntity;
    }
}
