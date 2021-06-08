package com.example.register.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.register.model.PostInfo;
import com.example.register.model.PostInfoRepository;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.register.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
public class MainController extends LoginController{
    @Autowired
    private PostInfoRepository postInfoRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CityRepository cityRepository;

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

    @GetMapping("foodMap")
    public String renderFoodMap(){return "food_index_maps";}

    @GetMapping("index")
    public String renderIndex(){return "index";}

    @GetMapping("profile")
    public String renderProfile(){return "profile";}

    @GetMapping("form")
    public String renderForm(){return "form";}

    @GetMapping("index1")
    public String renderIndex1(){return "index1";}

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
        System.out.println(posts.size());
        PostFetch res = new PostFetch();
        if(s+n>=posts.size()){
            List<Post> postFetches = new ArrayList<>();
            for(int i=s;i<posts.size();i++){
                System.out.println(posts.toString());
                postFetches.add(posts.get(i).getPost());
            }
            res.setPosts(postFetches);
            res.setEnd(true);
            System.out.println(res.toString());
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
        foodTag.addPost(postInfo);
        tagRepository.save(foodTag);
        if(hasMedia){
            for(int i=0;i<medias.length;i++){
                String sourcePath =  System.getProperty("user.dir")+"\\src\\main\\resources\\";;
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
                    System.out.println(e);
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
        Foodtag tag = tagRepository.findByName(content);
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

    @RequestMapping(value = "data/getImages")
    @ResponseBody
    public String getImagesAndLocations(){
        List<PostInfo> posts = postInfoRepository.findAll();
        List<Foodtag> tags = tagRepository.findAll();
//        存放所有的tag标签名
//        List<String> tagName = new ArrayList<>();
        int postSize = posts.size();
        int tagSize = tags.size();
        System.out.println(tagSize);
        Map<Integer, Integer> map = new HashMap<>();
//        获取tag与点赞数的map
        for(int i=0;i<postSize;i++){
//            判断帖子是否具有多媒体数据
//            计算帖子的点赞数
            if(mediaRepository.existsByPostInfo(postInfoRepository.findById(posts.get(i).getnId()).get())){
                int likeNum = likedRepository.findAllByPostId(posts.get(i).getnId()).size();
                map.put(posts.get(i).getnId(),likeNum);
            }

        }

        //这里将map.entrySet()转换成list
        List<Map.Entry<Integer,Integer>> list = new ArrayList<Map.Entry<Integer,Integer>>(map.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list,new Comparator<Map.Entry<Integer,Integer>>() {
            //降序排序
            public int compare(Map.Entry<Integer,Integer> n1,
                               Map.Entry<Integer,Integer> n2) {
                return n2.getValue()-n1.getValue();
            }

        });

        System.out.println(list.toString());

//        List<PostInfo> orderedPosts = postInfoRepository.findAllByOrderByLikedInfos();
        int num = list.size();
        List<String> rankInfos = new ArrayList();
//        JSONObject rankedObject = new JSONObject();

//        得到tag和对应图片的map
        Map<String,String > map_tag = new HashMap<>();
//        List<Map.Entry<Integer, Integer>> postRanked = list.subList(0,5);
        for (Map.Entry<Integer,Integer> mapping:list) {
            String tagName = postInfoRepository.findById(mapping.getKey()).get().getFoodtag().getName();
            String image = mediaRepository.findByPostInfo(postInfoRepository.findById(mapping.getKey()).get()).getPath();
            String city = tagRepository.findByName(tagName).getCity().getName();
            if(!map_tag.containsKey(tagName)){
                map_tag.put(tagName,image);
                rankInfos.add(tagName + "," + city + "," +image);
            }
//               rankedObject.put(userName,content);
        }

//
        return rankInfos.toString();
    }

    @RequestMapping(value = "data/RankedTags")
    @ResponseBody
    public String getLikeRankedTags(){
        List<PostInfo> posts = postInfoRepository.findAll();
        List<Foodtag> tags = tagRepository.findAll();
//        存放所有的tag标签名
//        List<String> tagName = new ArrayList<>();
        int postSize = posts.size();
        int tagSize = tags.size();
        System.out.println(tagSize);
        Map<String, Integer> map = new HashMap<>();
//        获取tag与点赞数的map
        for(int i=0;i<postSize;i++){
//            计算帖子的点赞数
            int likeNum = likedRepository.findAllByPostId(posts.get(i).getnId()).size();
            String tag = posts.get(i).getFoodtag().getName();
            if(map.containsKey(tag)){
                map.put(tag,map.get(tag)+likeNum);
            }
            else {
                map.put(tag,likeNum);
            }

        }
        //这里将map.entrySet()转换成list
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list,new Comparator<Map.Entry<String, Integer>>() {
            //降序排序
            public int compare(Map.Entry<String, Integer> n1,
                               Map.Entry<String, Integer> n2) {
                return n2.getValue()-n1.getValue();
            }

        });
        System.out.println(list.toString());

        int num = list.size();
        List<String> rankInfos = new ArrayList();

        if (num>2) {
            List<Map.Entry<String, Integer>> tagRanked = list.subList(0,3);
            for (Map.Entry<String, Integer> mapping:tagRanked) {
                String city = tagRepository.findByName(mapping.getKey()).getCity().getName();
                rankInfos.add(mapping.getKey() + "," + city + "," +mapping.getValue());
//                rankedObject.put(userName,content);
            }


        }
        else if(num>0 && num<3){
            List<Map.Entry<String, Integer>> postRanked = list.subList(0,num+1);

            for(Map.Entry<String, Integer> mapping:postRanked){
                String city = tagRepository.findByName(mapping.getKey()).getCity().getName();
                rankInfos.add(mapping.getKey() + "," + city + "," +mapping.getValue());

            }
        }


        return rankInfos.toString();
    }

    @RequestMapping(value = "data/likeRankedPosts")
    @ResponseBody
    public String getLikeRanked(){
//        这里尝试采用投影的做法来避免内存占用过大，but failed
//        List<PostInfo> postIds = (List<PostInfo>) postInfoRepository.findAllProjectedById();
        List<PostInfo> postIds = postInfoRepository.findAll();
        int postSize = postIds.size();
        System.out.println(postSize);
        Map<Integer, Integer> map = new HashMap<>();
        for(int i=0;i<postSize;i++){
            int likeNum = likedRepository.findAllByPostId(postIds.get(i).getnId()).size();
            map.put(postIds.get(i).getnId(),likeNum);
        }
        //这里将map.entrySet()转换成list
        List<Map.Entry<Integer,Integer>> list = new ArrayList<Map.Entry<Integer,Integer>>(map.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list,new Comparator<Map.Entry<Integer,Integer>>() {
            //降序排序
            public int compare(Map.Entry<Integer,Integer> n1,
                               Map.Entry<Integer,Integer> n2) {
                return n2.getValue()-n1.getValue();
            }

        });

        System.out.println(list.toString());

//        List<PostInfo> orderedPosts = postInfoRepository.findAllByOrderByLikedInfos();
        int num = list.size();
        List<String> rankInfos = new ArrayList();
//        JSONObject rankedObject = new JSONObject();

        if (num>4) {
            List<Map.Entry<Integer, Integer>> postRanked = list.subList(0,5);
            for (Map.Entry<Integer,Integer> mapping:postRanked) {
                String userName = postInfoRepository.findById(mapping.getKey()).get().getUser().getName();
                String content = postInfoRepository.findById(mapping.getKey()).get().getContent();
                rankInfos.add(userName + "," +content);
//                rankedObject.put(userName,content);
            }


        }
        else if(num>0 && num<5){
            List<Map.Entry<Integer, Integer>> postRanked = list.subList(0,num+1);

            for(Map.Entry<Integer,Integer> mapping:postRanked){
                String userName = postInfoRepository.findById(mapping.getKey()).get().getUser().getName();
                String content = postInfoRepository.findById(mapping.getKey()).get().getContent();
                rankInfos.add(userName + "," +content);
//                rankedObject.put(userName,content);

            }
        }

//
        System.out.println(rankInfos.toString());
        return rankInfos.toString();
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

    @RequestMapping(value = "data/findSearchType")
    @ResponseBody
    public int findSearchType(@RequestParam(value = "content") String content){
        User user = userRepository.findByName(content);
        Foodtag tag = tagRepository.findByName(content);
        if(user!=null){
            return 0;
        }
        else if(tag!=null){
            return 1;
        }
        else {
            return -1;
        }
    }

    @RequestMapping(value = "data/getUserIdByName")
    @ResponseBody
    public int getUserIdByName(@RequestParam(value = "username") String username){
        return userRepository.findByName(username).getId();
    }

    @RequestMapping(value = "data/saveTag")
    @ResponseBody
    public boolean saveTag(@RequestParam(value = "name") String name,
                        @RequestParam(value = "favor") String favor,
                        @RequestParam(value = "intro") String intro,
                        @RequestParam(value = "city") String city){
        if(!cityRepository.existsByName(city)){
            return false;
        }
        else {
            Foodtag tag = new Foodtag();
            tag.setDescription(intro);
            tag.setFavor(favor);
            tag.setName(name);
            tag.setCity(cityRepository.getByName(city));
            tagRepository.save(tag);
            return true;
        }
    }

    @RequestMapping(value = "data/saveCity")
    @ResponseBody
    public void saveCity(@RequestParam(value = "name") String name, @RequestParam(value = "lng") float lng
    ,@RequestParam(value = "lat") float lat){
        City  city = new City();
        city.setLat(lat);
        city.setLng(lng);
        city.setName(name);
        cityRepository.save(city);
    }

    @RequestMapping(value = "data/hasCity")
    @ResponseBody
    public boolean hasCity(){
        return cityRepository.findAll().size()==0;
    }

    @RequestMapping(value = "data/recommend")
    @ResponseBody
    public List<String> recommend(){
        System.out.println("recommending");
        List<Foodtag> foodTags = tagRepository.findAll();
        int n = Integer.min(4,foodTags.size());
        ArrayList<Integer> idxs = new ArrayList<>();
        List<String> res = new ArrayList<>();
        Random random = new Random();
        for (int i=0;i<n;i++){
//            int rd = (int)(Math.random()*foodTags.size());
            int rd = random.nextInt(foodTags.size());
            System.out.println(rd);
            if (!idxs.contains(rd)){
                res.add(foodTags.get(rd).getName());
                idxs.add(rd);
            }
            else {
                i--;
            }
        }
        return res;
    }
}

