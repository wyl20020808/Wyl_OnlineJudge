package com.wyl.backend;

import com.wyl.backend.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SqlTest {

    @Autowired
    UserMapper userMapper;

    /**
     * 测试mybatis是否能用
     */
    @Test
    void testMybatis() {
        System.out.println(userMapper.selectUById().toString());
    }

    /**
     * 测试mybatis-plus是否能用
     */
    @Test
    void testMP(){
        System.out.println(userMapper.selectList(null).toString());
    }

}
