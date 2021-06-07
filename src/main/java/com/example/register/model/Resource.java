package com.example.register.model;

import javax.persistence.*;

@Entity
@Table
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "type", nullable = false)
    private String type;
}
