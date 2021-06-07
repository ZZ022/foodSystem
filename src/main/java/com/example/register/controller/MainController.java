package com.example.register.controller;

import com.example.register.model.PostInfo;
import com.example.register.model.PostInfoRepository;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.register.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    LikedRepository likedRepository;


    public List<PostInfo> getInfo(int n){
        List<PostInfo> posts=postInfoRepository.findAllByOrderByDateDesc();
        int m = Integer.min(posts.size(), n);
        return posts.subList(0,m);
    }

    private boolean isPhoto(String fileType){
        if(Arrays.asList("tif","png","jpg","gif","tiff", "jfif", "pjpeg", "jpeg", "pjp").contains(fileType)){
            return true;
        }
        else {
            return false;
        }
    }

    @GetMapping("post")
    public String renderPost(){return "post";}

    @GetMapping("index")
    public String renderIndex(){return "index";}

    @GetMapping("profile")
    public String renderProfile(){return "profile";}

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
        return getInfo(5);
    }

    @RequestMapping(value = "data/fetchPost", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PostFetch sendPost(@RequestParam(value = "start") int s, @RequestParam(value = "num") int n){
        List<PostInfo> posts=postInfoRepository.findAllByOrderByDateDesc();
        PostFetch res = new PostFetch();
        if(s+n>=posts.size()){
            List<Post> postFetches = new ArrayList<>();
            for(int i=s;i<posts.size();i++){
                postFetches.add(posts.get(i).getPost());
            }
            res.setPosts(postFetches);
            res.setEnd(true);
            System.out.println(res);
            return res;
        }
        else {
            List<Post> postFetches = new ArrayList<>();
            for(int i=s;i<n+s;i++){
                postFetches.add(posts.get(i).getPost());
            }
            res.setPosts(postFetches);
            res.setEnd(false);
            System.out.println(res);
            return res;
        }

    }


    @RequestMapping("/api/likedAdd")
    @ResponseBody
    public boolean likedAdd(@RequestParam(value="postId") int postId, @RequestParam(value = "userId") int userId){
//        System.out.println(postId);
        System.out.println("I'm here");
        LikedInfo lInfo=new LikedInfo();
        lInfo.setUserId(userId);
        lInfo.setDate(new Date());
        lInfo.setPostId(postId);
        likedRepository.save(lInfo);
        System.out.println("liked success");
        return true;
    }

    @RequestMapping("/api/likedUpdated")
    @ResponseBody
    public int likedUpdate(@RequestParam(value = "postId") int postId){
        int numUpdated = likedRepository.countLikedInfosByPostId(postId);
        return numUpdated;
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
                           @RequestParam(value = "content") String content,
                              @RequestParam(value = "hasMedia") boolean hasMedia){
        User user = userRepository.findById(userId);
        Foodtag foodTag = tagRepository.findByName(tag);
        System.out.println(foodTag.getid());
        List<Media> localMedias = new ArrayList<>();
        Date date = new Date(System.currentTimeMillis());
        PostInfo postInfo = new PostInfo();
        postInfo.setDate(date);
        postInfo.setFoodtag(foodTag);
        postInfo.setUser(user);
        postInfo.setContent(content);
        postInfo.setLatitude(lat);
        postInfo.setLongtitude(lng);
        postInfoRepository.save(postInfo);
        if(hasMedia){
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
                    media.setPhoto(isPhoto(fileType));
                    System.out.println("saving media");
                    mediaRepository.save(media);
                    localMedias.add(media);
                }
                catch (Exception e){
                    return false;
                }
            }
            postInfo.setMedias(localMedias);
            postInfoRepository.save(postInfo);
        }
        return true;
    }

    @RequestMapping("data/search")
    @ResponseBody
    public PostFetch postsSerarch(@RequestParam(value = "content") String content){
        User user = userRepository.findByName(content);
        System.out.println(user);
        Foodtag tag = tagRepository.findByName(content);
        System.out.println(tag);
        if(user!=null){
            List<PostInfo> posts=postInfoRepository.findAllByUserOrderByDateDesc(user);
            PostFetch res = new PostFetch();
            if(100>=posts.size()){
                List<Post> postFetches = new ArrayList<>();
                for(int i=0;i<posts.size();i++){
                    postFetches.add(posts.get(i).getPost());
                }
                res.setPosts(postFetches);
                res.setEnd(true);
                System.out.println(res);
                return res;
            }
            else {
                List<Post> postFetches = new ArrayList<>();
                for(int i=0;i<100;i++){
                    postFetches.add(posts.get(i).getPost());
                }
                res.setPosts(postFetches);
                res.setEnd(false);
                System.out.println(res);
                return res;
            }
        }
        else if(tag != null){
            List<PostInfo> posts=postInfoRepository.findAllByFoodtagOrderByDateDesc(tag);
            PostFetch res = new PostFetch();
            if(100>=posts.size()){
                List<Post> postFetches = new ArrayList<>();
                for(int i=0;i<posts.size();i++){
                    postFetches.add(posts.get(i).getPost());
                }
                res.setPosts(postFetches);
                res.setEnd(true);
                System.out.println(res);
                return res;
            }
            else {
                List<Post> postFetches = new ArrayList<>();
                for(int i=0;i<100;i++){
                    postFetches.add(posts.get(i).getPost());
                }
                res.setPosts(postFetches);
                res.setEnd(false);
                System.out.println(res);
                return res;
            }
        }
        else {
            PostFetch res = new PostFetch();
            res.setPosts(new ArrayList<Post>());
            res.setEnd(false);
            return res;
        }
    }

    @RequestMapping(value = "api/isLiked")
    @ResponseBody
    public boolean isLiked(@RequestParam(value = "uid") int uid, @RequestParam(value = "postid") int postid){
        return likedRepository.existsByUserIdAndAndPostId(uid, postid);
    }

    @RequestMapping(value = "data/giveLike")
    @ResponseBody
    public int giveLike(@RequestParam(value = "uid") int uid){
        return likedRepository.countByUserId(uid);
    }

    @RequestMapping(value = "data/username")
    @ResponseBody
    public String getUser(@RequestParam(value = "uid") int uid){
        return userRepository.getById(uid).getName();
    }

    @RequestMapping(value = "data/getLike")
    @ResponseBody
    public int getLike(@RequestParam(value = "uid") int uid){
        int res = 0;
        List<PostInfo> posts = postInfoRepository.findAllByUser(userRepository.findById(uid));
        for(int i=0;i<posts.size();i++){
            res += likedRepository.countLikedInfosByPostId(posts.get(i).getnId());
        }
        return res;
    }

    @RequestMapping(value = "data/sign")
    @ResponseBody
    public String getSign(@RequestParam(value = "uid") int uid){
        return userRepository.getById(uid).getSign();
    }

    @RequestMapping(value = "data/saveSign")
    @ResponseBody
    public boolean saveSign(@RequestParam(value = "uid") int uid, @RequestParam(value = "sign") String sign){
        User user = userRepository.getById(uid);
        user.setSign(sign);
        userRepository.save(user);
        return true;
    }

}
