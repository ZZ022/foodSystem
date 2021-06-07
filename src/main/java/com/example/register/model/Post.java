package com.example.register.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {
    private int id;
    private String username;
    private String sign;
    private int likedNum;
    private String content;
    private String tag;
    private String date;
    private List<MediaFetch> medias;
    private float lon;
    private float lat;

    public Post(){}

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }

    public void setDate(String date) {
        this.date = date;
    }

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

    public void setMedias(List<MediaFetch> medias) {
        this.medias = medias;
    }

    public float getLon() {
        return lon;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public float getLat() {
        return lat;
    }

    public int getLikedNum() {
        return likedNum;
    }

    public List<MediaFetch> getMedias() {
        return medias;
    }

    public String getContent() {
        return content;
    }

    public String getTag() {
        return tag;
    }

    public String getUsername() {
        return username;
    }
}
