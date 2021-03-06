package com.example.register.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.register.model.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RegisterController {

    @Autowired private UserRepository userRepository;

    @GetMapping(value = "sign-up")
    public String renderRegister(){
        return "sign-up";
    }

    @RequestMapping(value = "register/submit")
    @ResponseBody
    public int register(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password){

        User user = new User();
        System.out.println(userRepository);
        if(userRepository.existsByName(username)){
            return -1;
        }
        user.setPassword(password);
        user.setName(username);
        userRepository.save(user);
        return user.getId();
    }

}
