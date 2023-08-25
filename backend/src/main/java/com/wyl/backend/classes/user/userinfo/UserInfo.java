package com.wyl.backend.classes.user.userinfo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@TableName("userinfo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo {
    @TableId(value = "userid")
    private int userid;
    private String username;
    private String userloginstate;
    private String password;
    private String registertime;
    private String useremail;
    private String token;
    private String usermotto;
    private String nickname;

    private String userpicture;

    public String getUserloginstate() {
        return userloginstate;
    }

    public void setUserloginstate(String userloginstate) {
        this.userloginstate = userloginstate;
    }

    public String getUserpicture() {
        return userpicture;
    }

    public void setUserpicture(String userpicture) {
        this.userpicture = userpicture;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUsermotto() {
        return usermotto;
    }

    public void setUsermotto(String usermotto) {
        this.usermotto = usermotto;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }



    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }



    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String userpassword) {
        this.password = userpassword;
    }

    public String getRegistertime() {
        return registertime;
    }

    public void setRegistertime(String registertime) {
        this.registertime = registertime;
    }
}
