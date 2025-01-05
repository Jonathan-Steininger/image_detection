package com.example.image_detection.data.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@ToString
@Table(name = "test_thing1", schema = "public")
public class TestThing1 {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

}
