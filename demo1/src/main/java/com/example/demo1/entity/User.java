package com.example.demo1.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private LocalDateTime createTime;
}
