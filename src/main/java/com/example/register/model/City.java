package com.example.register.model;

import javax.persistence.*;

@Entity
@Table(name = "t_City")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "lat")
    private float lat;

    @Column(name = "lng")
    private float lng;

    public float getLat() {
        return lat;
    }

    public int getId() {
        return id;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public float getLng() {
        return lng;
    }
}
