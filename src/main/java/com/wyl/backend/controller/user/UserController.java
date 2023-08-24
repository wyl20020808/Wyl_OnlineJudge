package com.wyl.backend.controller.user;

import com.wyl.backend.entity.user.UserInfo;
import com.wyl.backend.mapper.UserMapper;
import com.wyl.backend.utils.TimeUtil;
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
    private UserMapper userMapper;

    /**
     * 判断用户是否存在
     * @param username
     * @return
     */
    private Boolean exists(String username) {
        List<UserInfo> userinfo = userMapper.select();
        for (UserInfo userInfo : userinfo) {
            if (Objects.equals(userInfo.getUsername(), username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 用户注册
     * @param userInfo
     * @return
     */
    @PostMapping("/signin")
    public String signIn(@RequestBody UserInfo userInfo) {
        String formattedNow = TimeUtil.getNewTime();
        userInfo.setRegistertime(formattedNow);
        if (exists(userInfo.getUsername())) {
            return "用户名已存在";
        }

        int cnt = userMapper.insert(userInfo);
        if (cnt > 0)
            return "注册成功";
        return "error";
    }

    /**
     * 用户登录
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/login")
    public long login(@RequestBody UserInfo userInfo) {
        try {
            List<UserInfo> userinfo = userMapper.select();
            for (UserInfo u : userinfo) {
                System.out.println(u.getUsername());
                if (Objects.equals(u.getUsername(), userInfo.getUsername())) {
                    if (Objects.equals(u.getPassword(), userInfo.getPassword())) {
                        return u.getUserid();
                    } else {
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
