package com.example.register.model;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name="T_postInfo", indexes = {@Index(columnList = "date")})
public class PostInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int nId;
    @Column(name="userId")
    private int userId;
    @Column(name="date")
    private Date date;
    @Column(name="content")
    private String content;

    @OneToMany(targetEntity = LikedInfo.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "postId")
    private List<LikedInfo> likedInfos;

    public int getnId() {
        return nId;
    }

    public int getUserId() {
        return userId;
    }

    public PostInfo(User user, Date date, String content){
        this.userId = user.getId();
        this.date = date;
        this.content = content;
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
}
