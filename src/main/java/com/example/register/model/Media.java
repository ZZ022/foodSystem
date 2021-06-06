package com.example.register.model;

import com.fasterxml.jackson.databind.node.POJONode;

import javax.persistence.*;

@Entity
@Table(name = "t_Media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "path")
    private String path;

    @Column(name = "isPhoto")
    private boolean isPhoto;

    @ManyToOne(targetEntity = PostInfo.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "postId")
    PostInfo postInfo;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setPostInfo(PostInfo postInfo) {
        this.postInfo = postInfo;
    }

    public void setPhoto(boolean photo) {
        isPhoto = photo;
    }

    public boolean isPhoto() {
        return isPhoto;
    }
}
