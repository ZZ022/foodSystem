package com.example.register.model;

import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
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

    @OneToMany(targetEntity = CommentInfo.class, cascade = CascadeType.ALL)
    private List<CommentInfo> commentInfos;

    @ManyToOne(targetEntity = Foodtag.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "tagid")
    private Foodtag foodtag;

    @Column(name = "lat")
    private float latitude;

    @Column(name = "lon")
    private float longtitude;

    @OneToMany(targetEntity = Media.class, cascade = CascadeType.ALL)
    @JoinColumn
    private List<Media> medias;


    public PostInfo(){};

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


    public List<LikedInfo> getLikedInfos() {
        return likedInfos;
    }

    public List<CommentInfo> getCommentInfos() {
        return commentInfos;
    }

    public void setCommentInfos(List<CommentInfo> commentInfos) {
        this.commentInfos = commentInfos;
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

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    public Post getPost(){
        Post post = new Post();
        post.setId(nId);
        post.setLikedNum(likedInfos.size());
        post.setTag(foodtag.getName());
        post.setContent(content);
        post.setLat(latitude);
        post.setLon(longtitude);
        post.setUsername(user.getName());
        List<MediaFetch> paths = new ArrayList<MediaFetch>();
        for(int i=0;i<medias.size();i++){
            MediaFetch mediaFetch = new MediaFetch();
            mediaFetch.setMedia(medias.get(i).getPath());
            mediaFetch.setPhoto(medias.get(i).isPhoto());
            paths.add(mediaFetch);
        }
        post.setDate(date);
        post.setMedias(paths);
        return post;
    }
}
