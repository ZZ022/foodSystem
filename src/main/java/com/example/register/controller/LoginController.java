package com.example.register.controller;

import com.example.register.model.User;
import com.example.register.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.File;
import java.io.FileOutputStream;

public class LoginController{
    @Autowired protected UserRepository userRepository;

    @GetMapping("login")
    public String renderLogin(){return "sign-in";}

    @RequestMapping(value = "login/submit")
    @ResponseBody
    public String login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password){
        if(userRepository.existsByNameAndPassword(username,password)){
            User user;
            user = userRepository.findByNameAndPassword(username,password);
            if(!user.isLogin()){
                user.login();
                userRepository.save(user);
                return String.valueOf(user.getId());
            }
            else{
                return "login";
            }
        }
        else if(userRepository.existsByName(username)){
            return "password";
        }
        else {
            return "userName";
        }
    }
}
