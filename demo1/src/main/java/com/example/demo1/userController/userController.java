package com.example.demo1.userController;

import com.example.demo1.userMapper.userMapper;
import com.example.demo1.userService.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class userController {
    @Autowired
    private userService userService;

    //注册接口
    @PostMapping("/api/register")
    public String register(@RequestParam String username,@RequestParam String password){
        return userService.register(username,password);
    }
    //登录接口
    @PostMapping("/api/login")
    public String login(@RequestParam String username,@RequestParam String password){
        return userService.login(username,password);
    }
}
