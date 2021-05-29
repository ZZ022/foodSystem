package com.example.register.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="t_LikedInfo", uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "postId"})})
public class LikedInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int nId;
    @Column
    private int userId;
    @Column
    private int postId;
    @Column
    private Date date;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getnId() {
        return nId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
