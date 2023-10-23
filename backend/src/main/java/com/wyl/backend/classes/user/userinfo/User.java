package com.wyl.backend.classes.user.userinfo;

import lombok.Data;

@Data
public class User {
    public UserInfo userinfo;
    public UserExtra userextra;

    public User(UserInfo userInfo, UserExtra userExtra) {
        this.userinfo = userInfo;
        this.userextra = userExtra;
    }
}
