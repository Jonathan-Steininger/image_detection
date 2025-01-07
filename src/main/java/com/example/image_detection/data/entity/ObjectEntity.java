package com.example.image_detection.data.entity;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "object")
public class ObjectEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "name")
    private String name;

}
