package com.example.register.model;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.util.*;
import javax.persistence.criteria.CriteriaBuilder;


@Entity
@Table(name = "T_User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column(name = "Level")
    private int level;

    @Column(name = "name", length = 20, unique = true)
    private String name;

    @Column(name = "status")
    private boolean status;


    @OneToMany(targetEntity = PostInfo.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    List<PostInfo> postInfos;


    @Column(name = "password")
    private String password;

    public String getName() {
        return name;
    }

    public User(int id, String name, int level, boolean status, String password){
        this.Id = id;
        this.name = name;
        this.level = level;
        this.status = status;
        this.password = password;
    }

    public User(){
        this.level = 0;
        this.status = false;
    }


    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getLevel() {
        return level;
    }

    public boolean isLogin() {
        return status;
    }

    public int getId() {
        return Id;
    }

    public void login(){ setStatus(true);
    }

    public void logout(){
        setStatus(false);
    }


    public void changeLevel(int deltaLevel){
        setLevel(Integer.max(Integer.min(getLevel()+deltaLevel,5), 0));
    }

    public void upgrade(){
        setLevel(Integer.min(getLevel()+1,5));
    }

    public void downgrade(){
        setLevel(Integer.max(getLevel()-1,0));
    }
}

