package com.wyl.backend;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@SpringBootApplication
@MapperScan({"com.wyl.backend.classes.problem.controller",
        "com.wyl.backend.classes.problem.sql",
        "com.wyl.backend.classes.user.sql",
        "com.wyl.backend.classes.user.Controller",
})
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
