package com.example.register.controller;

import com.example.register.model.PostInfo;
import com.example.register.model.PostInfoRepository;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.register.model.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MainController extends LoginController{
    @Autowired
    private PostInfoRepository postInfoRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private MediaRepository mediaRepository;

    private int max_posts = 5;

    public List<PostInfo> getInfo(int n){
        List<PostInfo> posts=postInfoRepository.findAllByOrderByDateDesc();
        int m = Integer.min(posts.size(), n);
        return posts.subList(0,m);
    }
    @Autowired
    LikedRepository likedRepository;

    @GetMapping("post")
    public String renderPost(){return "post";}

    @GetMapping("index")
    public String renderIndex(){return "index";}

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


    @RequestMapping("data/submit")
    @ResponseBody
    public String submit(@RequestParam(value = "post") String post, @RequestParam(value = "uid") int userId){
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

    @RequestMapping("data/addTag")
    @ResponseBody
    public List<String> addTag(){
        List<Foodtag> foodTags = tagRepository.findAll();
        List<String> foodStrings = new ArrayList<>();
        for(int i=0;i<foodTags.size();i++){
            foodStrings.add(foodTags.get(i).getName());
        }
        System.out.println(foodStrings);
        return foodStrings;
    }

    @RequestMapping("data/submitPost")
    @ResponseBody
    public boolean submitPost(@RequestParam(value = "uid") int userId,@RequestParam(value = "tag") String tag,
                              @RequestParam(value = "lat") float lat, @RequestParam(value = "lng") float lng,
                           @RequestParam(value = "medias[]") MultipartFile[] medias,
                           @RequestParam(value = "content") String content){
        User user = userRepository.findById(userId);
        Foodtag foodTag = tagRepository.findByName(tag);
        Date date = new Date(System.currentTimeMillis());
        PostInfo postInfo = new PostInfo();
        for(int i=0;i<medias.length;i++){
            String sourcePath =  "E:\\课程\\大三下\\gis工程\\实习\\foodSystem\\src\\main\\resources\\";
            String path = "media\\"+postInfo.getnId() + "_" + i +'.';
            String originalFileName = medias[i].getOriginalFilename();
            String fileType = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
            path += fileType;
            File file = new File(sourcePath+path);
            try {
                medias[i].transferTo(file);
                Media media = new Media();
                media.setPostInfo(postInfo);
                media.setPath(path);
                mediaRepository.save(media);
            }
            catch (Exception e){
                return false;
            }

        }
        postInfo.setDate(date);
        postInfo.setFoodtag(foodTag);
        postInfo.setUser(user);
        postInfo.setContent(content);
        postInfo.setLatitude(lat);
        postInfo.setLongtitude(lng);
        postInfoRepository.save(postInfo);
        return true;
    }
}
