package com.example.demo1.userMapper;
import com.example.demo1.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface userMapper {
    @Select("SELECT * FROM users WHERE username=#{username}")
    User selectUser(String name);

    @Insert("INSERT INTO users (username,password,create_time) VALUES (#{username},#{password},#{createTime})")
    int insertUser(User user);

}
