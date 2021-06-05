package com.example.register.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "t_Tag")
public class Foodtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "city",nullable = false)
    private String city;

    @OneToMany(targetEntity = PostInfo.class, cascade = CascadeType.ALL)
    private List<PostInfo> postInfos;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getid() {
        return id;
    }

    public List<PostInfo> getPostInfos() {
        return postInfos;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }
}
