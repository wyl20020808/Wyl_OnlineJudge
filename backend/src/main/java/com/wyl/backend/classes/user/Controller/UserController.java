package com.wyl.backend.classes.user.Controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyl.backend.classes.user.sql.UserOperator;
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
    public String getUsername(int userid) {
        return userOperator.selectById(userid).getUsername();
    }
    @PostMapping("/query")
    public UserInfo queryUserInfo(@RequestBody UserInfo userInfo) {
        return userOperator.selectById(userInfo.getUserid());
    }

    @PostMapping("/synchronize/userinfo")
    public String synchronizeUserInfo(@RequestBody UserInfo userInfo) {
        try{
            userOperator.updateById(userInfo);
            return "success";
        }catch (Exception e){
            return "error";
        }
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

    @PostMapping("/update/cardinfo")
    public int updateUsename(@RequestBody UserInfo userInfo) {
//        System.out.println(userInfo.getNickname());
        int cnt = userOperator.updateUsername(userInfo);
        if(cnt > 0)
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
