package com.wyl.backend.entity.user;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

import static com.baomidou.mybatisplus.annotation.IdType.ASSIGN_UUID;

/**
 * 用户实体类
 */
@TableName("userinfo")
@Data
@Builder
public class UserInfo implements Serializable {
    @TableId(type = ASSIGN_UUID)
    private long userid;
    private String username;
    private String password;
    private DateTime registertime;
    private String useremail;
    @TableField(exist = false)
    private String token;
    @TableLogic(value="0",delval="1")
    private int deleted;

}
