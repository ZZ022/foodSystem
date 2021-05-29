package com.example.register.model;

import java.io.File;
import java.util.List;

public class Post {
    private int id;
    private String username;
    private int likedNum;
    private String content;
    private String tag;
    private List<String> medias;
    private float lon;
    private float lat;




    public Post(){}

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLikedNum(int likedNum) {
        this.likedNum = likedNum;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setMedias(List<String> medias) {
        this.medias = medias;
    }
}
