package com.wyl.backend.classes.user.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyl.backend.classes.user.pojo.UserProtocol;
import com.wyl.backend.classes.user.sql.UserExtraOperator;
import com.wyl.backend.classes.user.sql.UserOperator;
import com.wyl.backend.classes.user.userinfo.User;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@CrossOrigin
@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired //注入，一定要写
    private UserOperator userOperator;
    @Autowired
    private UserExtraOperator userExtraOperator;

    private Boolean exists(String username) {
        List<UserInfo> userinfo = userOperator.select();
        for (UserInfo userInfo : userinfo) {
            if (Objects.equals(userInfo.getUsername(), username)) {
                return true;
            }
        }
        return false;
    }

    public String getUsername(int userid) {
        return userOperator.selectById(userid).getUsername();
    }

    @PostMapping("/query")
    public UserInfo queryUserInfo(@RequestBody UserInfo userInfo) {
        return userOperator.selectById(userInfo.getUserid());
    }

    @GetMapping("/query/all")
    public List<User> queryUserAll() {
        List<User> user = new ArrayList<>();
        List<UserInfo> userinfo = userOperator.selectList(null);
        List<UserExtra> extra = userExtraOperator.selectList(null);
        for (int i = 0; i < userinfo.size(); i++) {
            UserInfo userInfo = userinfo.get(i);
            UserExtra userExtra = extra.get(i);
            // 在这里，i 就是元素的下标
            user.add(new User(userInfo, userExtra));
        }
        return user;
    }

    @GetMapping("/query/alluser")
    public List<UserInfo> queryAllUserInfo() {
        return userOperator.selectList(null);
    }


    @PostMapping("/synchronize/userinfo")
    public String synchronizeUserInfo(@RequestBody UserInfo userInfo) {
        try {
            userOperator.updateById(userInfo);
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }

    /**
     * 注册用户
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/signin")
    public String signIn(@RequestBody UserInfo userInfo) {

        //获取当前时间，并匹配SQL的格式
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        userInfo.setRegistertime(formattedNow);

        if (exists(userInfo.getUsername())) {
            return "用户名已存在";
        }

        int cnt = userOperator.insert(userInfo);
        if (cnt > 0)
            return "注册成功";
        return "error";
    }

    /**
     * 登录接口
     * 成功登录返回用户id，用户不存在返回0
     * 出现异常返回-1
     * 其他情况返回-2
     * @param info
     * @return
     */
    @PostMapping("/login")
    public int login(@RequestBody UserProtocol.UserLoginInfoInput info) {
        //用来暂存从数据库获取到的信息
        UserInfo userInfo = null;

        try {
            //从数据库中查询用户姓名与密码
            LambdaQueryWrapper<UserInfo> queryWrapper = Wrappers.lambdaQuery();
            //获取匹配名字和密码的用户id
            queryWrapper
                    .select(UserInfo::getUserid)
                    .eq(UserInfo::getUsername, info.getUsername())
                    .eq(UserInfo::getPassword, info.getPassword())
            ;

            userInfo = userOperator.selectOne(queryWrapper);

            if (userInfo != null) {
                return userInfo.getUserid();
            } else {
                return 0;
            }
        } catch (Exception e) {
            return -1;
        }
    }

    @PostMapping("/update/cardinfo")
    public int updateUsename(@RequestBody UserInfo userInfo) {
//        System.out.println(userInfo.getNickname());
        int cnt = userOperator.updateUsername(userInfo);
        if (cnt > 0)
            return 1;
        return 0;
    }

    @PostMapping("/update/userpicture")
    public int updateUserpicture(@RequestBody UserInfo userInfo) {
        return userOperator.updateUserPictrue(userInfo);
    }

    @PostMapping("/image/upload")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("userinfo") String userinfo) {
        try {
            // 获取文件名
            String fileName = file.getOriginalFilename();

            // 指定文件保存的位置。这里我们保存在项目的根目录下的一个 'uploads' 文件夹中
            String folder = System.getProperty("user.dir") + "/uploads/";
//            System.out.println(folder);
            // 获取文件的后缀名
            ObjectMapper objectMapper = new ObjectMapper();

            // 使用 ObjectMapper 将 JSON 字符串转换为 User 对象
            UserInfo user = objectMapper.readValue(userinfo, UserInfo.class);
            String newFileName = new String(String.valueOf(user.getUserid())) + fileName.substring(fileName.lastIndexOf("."));
//            System.out.println(newFileName);
            Path path = Paths.get(folder + newFileName);

            // 检查 'uploads' 文件夹是否存在，如果不存在则创建
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }

            // 将文件保存到目标位置
            Files.write(path, file.getBytes());
            // 返回文件的 URL，这取决于你的应用的需求
            String env = System.getProperty("env");

            if ("production".equals(env)) {
                return "http://8.134.48.157:8088/images/" + newFileName;
                // 生产环境
            }
            // 开发环境
            return "http://localhost:8088/images/" + newFileName;


//            System.out.println("http://localhost:8088/images/" + fileName);

        } catch (Exception e) {
            e.printStackTrace();
            return "Upload failed";
        }
    }
}
