package com.example.demo1.userService;

import com.example.demo1.entity.User;
import com.example.demo1.userMapper.userMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class userService {
    @Autowired
    private userMapper userMapper;

    //密码加密器(使用Spring Security提供的BCryptPasswordEncoder)
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //JWT密钥
    private final SecretKey jwtKry = Keys.hmacShaKeyFor("your-secret-key-1234567890123456".getBytes());

    //注册功能
    public String register(String username, String password) {
        //1.基础验证
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return "用户名和密码不能为空";
        }
        //2.检查用户是否已存在
        User existUser = userMapper.selectUser(username);
        if (existUser != null) {
            return "用户名已存在";
        }
        //3.加密密码
        String enctyptedPwd = passwordEncoder.encode(password);
        //4.创建用户对象并保存到数据库
        User user = new User();
        user.setUsername(username);
        user.setPassword(enctyptedPwd);
        user.setCreateTime(LocalDateTime.now());
        int result = userMapper.insertUser(user);
        return result > 0 ? "注册成功" : "注册失败";
    }

    //登录功能
    public String login(String username, String password) {
        //1.基础验证
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return "用户名和密码不能为空";
        }
        //2.查询用户
            User user = userMapper.selectUser(username);
            if(user==null){
                return "用户或密码错误";
            }
        //3.验证密码(加密对比)
        boolean isPwdCorrect = passwordEncoder.matches(password,user.getPassword());
            if(!isPwdCorrect){
                return "用户或密码错误";
            }
        //4.生成JWT令牌(登录成功返回令牌)
            String token= Jwts.builder()
                    //1.主体：谁的令牌(用户名)
                    .setSubject(user.getUsername())
                    //2.过期时间：1小时后
                    .setExpiration(new Date(System.currentTimeMillis() + 3600*1000))
                    //3.签名：使用密钥和算法进行签名，防止篡改
                    .signWith(SignatureAlgorithm.HS256, jwtKry)
                    //4.构建并返回令牌字符串
                    .compact();
            return "登录成功，令牌："+token;
    }
}


