package com.example.register;

import com.example.register.model.PostInfo;
import com.example.register.model.PostInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class RegisterApplicationTests {

    void contextLoads() {
    }
    @Autowired
    private PostInfoRepository postInfoRepository;

    public void AddPosts(){
        List<PostInfo> posts = new ArrayList<>();
        for(int i = 0;i < 10; i++){
            posts.set(i,postInfoRepository.save(new PostInfo()));
        }
    }



    }


