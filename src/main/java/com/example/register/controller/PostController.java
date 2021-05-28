package com.example.register.controller;

import com.example.register.model.PostInfo;
import com.example.register.model.PostInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.data.domain.Sort;
=======
>>>>>>> 6f5c4cc... first commit
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.register.model.*;

<<<<<<< HEAD
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;
import java.util.List;
=======
import java.sql.SQLOutput;
import java.util.Date;
>>>>>>> 6f5c4cc... first commit

@Controller
public class PostController extends LoginController{
    @Autowired
    PostInfoRepository postInfoRepository;
<<<<<<< HEAD

    private int max_posts = 5;

    public List<PostInfo> getInfo(int n){
        List<PostInfo> posts=postInfoRepository.findAllByOrderByDateDesc();
        int m = Integer.min(posts.size(), n);
        return posts.subList(0,m);
    }
=======
    @Autowired
    LikedRepository likedRepository;
>>>>>>> 6f5c4cc... first commit

    @GetMapping("post")
    public String renderPost(){return "post";}

<<<<<<< HEAD
    @GetMapping("food")
    public String renderSurf(){return "surf";}

=======
>>>>>>> 6f5c4cc... first commit
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
<<<<<<< HEAD
        System.out.println(getInfo(4));
=======
>>>>>>> 6f5c4cc... first commit
        System.out.println(id);
        return "success";
    }

<<<<<<< HEAD
    @RequestMapping("post/surf")
    @ResponseBody
    public List<PostInfo> surf(){
        return getInfo(max_posts);
    }

=======

    @RequestMapping("/api/likedAdd")
    @ResponseBody
    public void likedAdd(@RequestParam(value="btnid") int postId, @RequestParam(value = "uid") int userId){
        System.out.println(postId);
        LikedInfo lInfo=new LikedInfo();
        lInfo.setUserId(userId);
        lInfo.setDate(new Date());
        lInfo.setPostId(postId);
        likedRepository.save(lInfo);
        System.out.println("liked success");
    }



>>>>>>> 6f5c4cc... first commit
}
