package com.example.image_detection.service;

import com.google.cloud.vision.v1.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetectLabelsService {

    // default number of labels returned by the google api
    private final static int DEFAULT_LABELS_LIST_SIZE = 10;

    public static List<String> detectLabels(Image image, Boolean enableObjectDetection) throws IOException {
        if (enableObjectDetection == null || Boolean.FALSE.equals(enableObjectDetection)) return null;
        if (image == null) return null;

        Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(image).build();
        List<AnnotateImageRequest> requests = List.of(request);

        List<String> labels = new ArrayList<>(DEFAULT_LABELS_LIST_SIZE);
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    throw new RuntimeException(res.getError().getMessage());
                }

                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    labels.add(annotation.getDescription().toLowerCase());
                }
            }
        }
        return labels;
    }
}
