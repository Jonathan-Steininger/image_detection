package com.example.image_detection.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image_objects_view")
public class ImageObjectsViewEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "label")
    private String label;

    @Type(type = "string-array")
    @Column(name = "objects", columnDefinition = "text[]")
    private String[] objects;

}
