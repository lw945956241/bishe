package com.qyhy;

import com.qyhy.entity.User;
import com.qyhy.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SchoolApplicationTests {


    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        userMapper.insert(new User().setName("asd"));
    }

}
