package com.example.image_detection.data.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@Entity
@Table(name = "image")
public class ImageEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "label")
    private String label;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "image_bytes", length = 5000)
    private byte[] imageBytes;

}
