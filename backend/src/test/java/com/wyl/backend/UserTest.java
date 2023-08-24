package com.wyl.backend;

import com.wyl.backend.entity.user.UserInfo;
import com.wyl.backend.mapper.UserMapper;
import com.wyl.backend.utils.TimeUtil;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.beans.Transient;

@SpringBootTest
public class UserTest {
    @Autowired
    UserMapper userMapper;
    @Test
    void testSign(){
        UserInfo userInfo = UserInfo.builder()
                .username("fff")
                .password("111")
                .registertime(TimeUtil.getNewTime())
                .useremail("jfskalfjk@fksla.com")
                .build();
        int count =  userMapper.insert(userInfo);
        System.out.println(count == 1 ? "插入成功":"插入失败");
    }
}
