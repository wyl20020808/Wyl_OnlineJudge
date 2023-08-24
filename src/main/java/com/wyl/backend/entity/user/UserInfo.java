package com.wyl.backend.entity.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

import static com.baomidou.mybatisplus.annotation.IdType.ASSIGN_UUID;

/**
 * 用户实体类
 */
@TableName("userinfo")
@Data
@Builder
public class UserInfo implements Serializable {
    @TableId(type = ASSIGN_UUID)
    private Long userid;
    private String username;
    private String password;
    private String registertime;
    private String useremail;
    @TableField(exist = false)
    private String token;

}
