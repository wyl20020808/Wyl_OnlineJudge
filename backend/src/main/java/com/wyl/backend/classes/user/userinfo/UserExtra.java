package com.wyl.backend.classes.user.userinfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("userextra")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserExtra {
    private int extraid;
    private int userid;
    private int submitcount;
    private int aceptedcount;

    private int fans;
    private int concern;
    private int rating;
    private int ranking;
    @TableField(exist = false)
    private String special;


}
