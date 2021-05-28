package com.example.register;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.register.controller.*;
@SpringBootTest
class RegisterApplicationTests {

    @Test
    void contextLoads() {
        PostController P = new PostController();
        System.out.println(P.getInfo(3));
    }


}
