package com.example.register.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_Tag")
public class Foodtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ManyToOne(targetEntity = City.class)
    @JoinColumn(name = "city",nullable = false)
    City city;

    @OneToMany(targetEntity = PostInfo.class, cascade = CascadeType.ALL)
    private List<PostInfo> postInfos = new ArrayList<>();

    @Column(name = "favor")
    private String favor;

    @Column(name = "description")
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFavor(String favor) {
        this.favor = favor;
    }

    public String getDescription() {
        return description;
    }

    public String getFavor() {
        return favor;
    }

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

    public void setCity(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    public void addPost(PostInfo postInfo){
        this.postInfos.add(postInfo);
    }
}
