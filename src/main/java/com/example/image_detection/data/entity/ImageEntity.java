package com.example.image_detection.data.entity;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Table(name = "image")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "string-array", typeClass = StringArrayType.class)
public class ImageEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "label")
    private String label;

    @Type(type = "string-array")
    @Column(name = "objects", columnDefinition = "text[]")
    private String[] objects;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "image_bytes", length = 5000)
    private byte[] imageBytes;

}
