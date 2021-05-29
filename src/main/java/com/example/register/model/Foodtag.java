package com.example.register.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "t_Tag")
public class Foodtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(targetEntity = PostInfo.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "nId")
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
}
