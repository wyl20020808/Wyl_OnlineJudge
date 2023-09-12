package com.wyl.backend;

import com.wyl.backend.utils.FileUtil;
import com.wyl.backend.utils.SnowIdUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@SpringBootTest
public class UtilTest {
    @Resource
    SnowIdUtil userIdGenerator;
    @Resource
    SnowIdUtil problemIdGenerator;
    @Test
    void snowGetIDTest(){

        long id;
        for (int i = 0; i < 10; i++) {
            id = userIdGenerator.nextId();
            System.out.println(id);
        }
        System.out.println("=============================");
        for (int i = 0; i < 10; i++) {
            id = problemIdGenerator.nextId();
            System.out.println(id);
        }
    }

    @Test
    void deletFolder() throws IOException {
        FileUtil.deletDir("D:\\CodeLife\\Code\\webStudy\\code\\Wyl_OnlineJudge\\Wyl_OnlineJudge_Hou\\target\\classes\\cache\\c3f63779-b5aa-4245-a2b5-71e6b85ed5be");
    }
}
