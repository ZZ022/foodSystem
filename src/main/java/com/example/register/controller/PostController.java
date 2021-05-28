package com.example.register.controller;

import com.example.register.model.PostInfo;
import com.example.register.model.PostInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.register.model.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;
import java.util.List;

@Controller
public class PostController extends LoginController{
    @Autowired
    PostInfoRepository postInfoRepository;

    private int max_posts = 5;

    public List<PostInfo> getInfo(int n){
        List<PostInfo> posts=postInfoRepository.findAllByOrderByDateDesc();
        int m = Integer.min(posts.size(), n);
        return posts.subList(0,m);
    }

    @GetMapping("post")
    public String renderPost(){return "post";}

    @GetMapping("food")
    public String renderSurf(){return "surf";}

    @RequestMapping("data/logout")
    @ResponseBody
    public void logout(@RequestParam(value = "uid") int userId){
        User user = userRepository.findById(userId);
        user.logout();
        userRepository.save(user);
        System.out.println("logout");
    }

    @RequestMapping("data/login")
    @ResponseBody
    public void login(@RequestParam(value = "uid") int userId){
        User user = userRepository.findById(userId);
        user.login();
        userRepository.save(user);
        System.out.println("login");
    }


    @RequestMapping("post/submit")
    @ResponseBody
    public String submit(@RequestParam(value = "post") String post, @RequestParam(value = "uid") int userId){
        User user = userRepository.findById(userId);
        Date date = new Date(System.currentTimeMillis());
        PostInfo postInfo = new PostInfo(user, date, post);
        postInfoRepository.save(postInfo);
        return "success";
    }

    @RequestMapping("test/favor")
    @ResponseBody
    public String favor(@RequestParam(value = "btnid") int id){
        System.out.println(getInfo(4));
        System.out.println(id);
        return "success";
    }

    @RequestMapping("post/surf")
    @ResponseBody
    public List<PostInfo> surf(){
        return getInfo(max_posts);
    }

}
