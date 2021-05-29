package com.example.register.model;

import javax.persistence.*;

@Entity
@Table(name = "t_Media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "path")
    private String path;

    @ManyToOne(targetEntity = PostInfo.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "nId")
    private  PostInfo postInfo;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setPostInfo(PostInfo postInfo) {
        this.postInfo = postInfo;
    }
}
