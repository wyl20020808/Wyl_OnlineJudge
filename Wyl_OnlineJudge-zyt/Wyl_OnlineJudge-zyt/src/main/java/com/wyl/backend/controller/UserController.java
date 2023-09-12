package com.wyl.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wyl.backend.entity.user.UserInfo;
import com.wyl.backend.entity.utils.Result;
import com.wyl.backend.mapper.UserMapper;
import com.wyl.backend.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@CrossOrigin
@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired //注入，一定要写
    private UserMapper userMapper;

    /**
     * 判断用户名是否存在
     *
     * @param username 用户名
     * @return  用户名是否存在
     */
    private Boolean exists(String username) {
        QueryWrapper<UserInfo> lqw = new QueryWrapper<UserInfo>();
        lqw.apply("name=" + username);
        UserInfo userInfo = userMapper.selectOne(lqw);
        return userInfo != null;
    }

    /**
     * 用户注册
     *
     * @param userInfo 用户信息
     * @return 注册结果
     */
    @PostMapping("/signin")
    public Result signIn(@RequestBody UserInfo userInfo) {
        if (exists(userInfo.getUsername())) {
            return Result.error("用户名已存在");
        }
        userInfo.setRegistertime(TimeUtil.getNewDateTime());

        //应该对邮箱啥的进行正则校验

        //应该对密码进行加密

        int cnt = userMapper.insert(userInfo);
        if (cnt > 0)
            return Result.success("注册成功");
        return Result.error("用户创建失败");
    }


    /**
     * 用户登录
     *
     * @param loginInfo 用户信息
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserInfo loginInfo) {
        UserInfo userInfo = userMapper.selectById(loginInfo.getUserid());
        if (userInfo == null)
            return Result.error("用户不存在");

        //应该对密码进行加密

        if (Objects.equals(userInfo.getPassword(), loginInfo.getPassword())) {
            //写入jwt

            return Result.success("登录成功");
        }

        return Result.error("登录失败，请校验密码");
    }
}
