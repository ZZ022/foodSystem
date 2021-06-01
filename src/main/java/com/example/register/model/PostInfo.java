package com.example.register.model;

import javafx.geometry.Pos;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name="T_postInfo", indexes = {@Index(columnList = "date")})
public class PostInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int nId;

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private User user;

    @Column(name="date")
    private Date date;

    @Column(name="content")
    private String content;

    @OneToMany(targetEntity = LikedInfo.class, cascade = CascadeType.ALL)
    private List<LikedInfo> likedInfos;

    @ManyToOne(targetEntity = Foodtag.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "tagid")
    private Foodtag foodtag;

    @Column(name = "lat")
    private float latitude;

    @Column(name = "lon")
    private float longtitude;

    @OneToMany(targetEntity = Media.class, cascade = CascadeType.ALL)
    private List<Media> medias;

    public int getnId() {
        return nId;
    }

    public User getUser() {
        return user;
    }

    public float getLon() {
        return longtitude;
    }

    public void setLongtitude(float longtitude) {
        this.longtitude = longtitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFoodtag(Foodtag foodtag) {
        this.foodtag = foodtag;
    }

    public PostInfo(){};

    public List<LikedInfo> getLikedInfos() {
        return likedInfos;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Post getPost(){
        Post post = new Post();
        post.setId(nId);
        post.setLikedNum(likedInfos.size());
        post.setTag(foodtag.getName());
        post.setContent(content);
        post.setLat(latitude);
        post.setLon(longtitude);
        List<String> paths = new ArrayList<String>();
        for(int i=0;i<medias.size();i++){
            paths.add(medias.get(i).getPath());
        }
        post.setMedias(paths);
        return post;
    }
}
