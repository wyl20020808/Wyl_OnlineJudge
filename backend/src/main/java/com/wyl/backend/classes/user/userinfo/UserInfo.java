package com.wyl.backend.classes.user.userinfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
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
    private String userphone;
    private String usermotto;
    private String nickname;
    private String userpicture;
    @TableField(select = false)
    private String special;//更新单个数据，借助它来写

}
