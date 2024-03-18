package com.wyl.backend;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@SpringBootApplication
@EnableTransactionManagement
@MapperScan({"com.wyl.backend.classes.problem.controller",
        "com.wyl.backend.classes.problem.sql",
        "com.wyl.backend.classes.user.sql",
        "com.wyl.backend.classes.user.Controller",
        "com.wyl.backend.classes.judge.controller",
        "com.wyl.backend.classes.judge.sql",
        "com.wyl.backend.classes.contest.SQL",
        "com.wyl.backend.classes.contest.controller",
        "com.wyl.backend.classes.message.Controller",
        "com.wyl.backend.classes.message.SQL",
        "com.wyl.backend.classes.discuss",
        "com.wyl.backend.classes.Collect",
        "com.wyl.backend.classes.limit",
})
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
