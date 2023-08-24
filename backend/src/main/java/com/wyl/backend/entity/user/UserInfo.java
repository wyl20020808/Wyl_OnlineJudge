package com.wyl.backend.entity.user;

import lombok.Data;

@Data
public class UserInfo {
    private int userid;
    private String username;
    private String password;
    private String registertime;
    private String useremail;
    private String token;
}
