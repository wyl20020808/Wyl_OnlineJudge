package com.wyl.backend.entity.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("userinfo")
@Data
public class UserInfo {
    @TableId
    private int userid;
    private String username;
    private String password;
    private String registertime;
    private String useremail;
    @TableField(exist = false)
    private String token;
}
