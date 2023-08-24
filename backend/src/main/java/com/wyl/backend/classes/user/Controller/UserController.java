package com.wyl.backend.classes.user.Controller;

import com.wyl.backend.classes.user.sql.UserOperator;
import com.wyl.backend.classes.user.userinfo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired //注入，一定要写
    private UserOperator userOperator;

    private Boolean exists(String username) {
        List<UserInfo> userinfo = userOperator.select();
        for (UserInfo userInfo : userinfo) {
            if (Objects.equals(userInfo.getUsername(), username)) {
                return true;
            }
        }
        return false;
    }
    @PostMapping("/signin")
    public String signIn(@RequestBody UserInfo userInfo) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        userInfo.setRegistertime(formattedNow);
        if(exists(userInfo.getUsername())){
            return "用户名已存在";
        }

        int cnt = userOperator.insert(userInfo);
        if(cnt > 0)
            return "注册成功";
        return "error";
    }

    @PostMapping("/login")
    public int login(@RequestBody UserInfo userInfo) {
        try {
            List<UserInfo>userinfo = userOperator.select();
            for(UserInfo u:userinfo) {
                System.out.println(u.getUsername());
                if(Objects.equals(u.getUsername(), userInfo.getUsername())){
                    if(Objects.equals(u.getPassword(), userInfo.getPassword())){
                        return u.getUserid();
                    }else{
                        return 0;
                    }
                }
            }
        } catch (Exception e) {
            return -1;
        }
        return -2;
    }
}
