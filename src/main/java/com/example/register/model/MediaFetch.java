package com.example.register.model;

public class MediaFetch {
    private String path;
    private boolean isPhoto;

    public void setPhoto(boolean photo) {
        isPhoto = photo;
    }

    public void setMedia(String media) {
        this.path = media;
    }

    public String getPath() {
        return path;
    }

    public boolean isPhoto() {
        return isPhoto;
    }
}
