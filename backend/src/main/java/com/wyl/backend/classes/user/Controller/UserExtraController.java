package com.wyl.backend.classes.user.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyl.backend.classes.user.sql.UserExtraOperator;
import com.wyl.backend.classes.user.sql.UserOperator;
import com.wyl.backend.classes.user.userinfo.UserExtra;
import com.wyl.backend.classes.user.userinfo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
@JsonIgnoreProperties(ignoreUnknown = true)
@CrossOrigin
@RequestMapping("/userextra")
@RestController
public class UserExtraController {
    @Autowired //注入，一定要写
    private UserExtraOperator userExtraOperator;
    @Autowired
    private UserOperator userOperator;
    @PostMapping("/update/special")
    public void updateSpecial(@RequestBody UserExtra userExtra){
        System.out.println(userExtra.toString());
         if(userExtra.getSpecial().equals("submitcount"))
             userExtraOperator.incrementSubmitCount(userExtra.getUserid());
         else if(userExtra.getSpecial().equals("aceptedcount"))
             userExtraOperator.incrementAceptedcountCount(userExtra.getUserid());
         else {
             UpdateWrapper
                     update = new UpdateWrapper<>();
             update.eq("userid",userExtra.getUserid());
             userExtraOperator.update(userExtra,update);
         }
    }
    @GetMapping("/query/id")
    public UserExtra queryById(@RequestParam("userid") int userid){
        QueryWrapper
                <UserExtra> query = new QueryWrapper<>();
        query.eq("userid",userid);
        return
                userExtraOperator.selectOne(query);
    }

    @PostMapping("/synchronizeinfo")//用于同步两个表中id的信息
    public void insert(@RequestBody  UserExtra userextra){
        List<UserInfo> userInfoList = userOperator.selectList(null);
        List<UserExtra>
                userExtraList = userExtraOperator.selectList(null);
        for (UserInfo userInfo : userInfoList) {
            // 检查userExtraList中是否存在相同的userid
            boolean exists = userExtraList.stream().anyMatch(extra -> extra.getUserid() == userInfo.getUserid());
            // 如果不存在，就添加到userExtraList中
            if (!exists) {
                UserExtra userExtra = new UserExtra();
                userExtra.setUserid(userInfo.getUserid());
                userExtra.setSubmitcount(0);
                userExtra.setAceptedcount(0);
                // 设置其他需要的字段
                // ...
                userExtraOperator.insert(userExtra);
            }
        }
    }
}
