package com.wyl.backend.entity.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * 用户实体类
 */
@TableName("userinfo")
@Data
@Builder
public class UserInfo {
    @TableId
    private Long userid;
    private String username;
    private String password;
    private String registertime;
    private String useremail;
    @TableField(exist = false)
    private String token;

}
