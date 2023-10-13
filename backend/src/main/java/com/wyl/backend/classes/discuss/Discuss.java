package com.wyl.backend.classes.discuss;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("discuss")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Discuss {
    @TableId(value = "id")
    public int id;
    public int userid;
    public String username;
    public String title;
    public String content;
    public String type;
    public String createtime;
    public String edittime;
    public String userpicture;
    public String targetname;
    public String targettype;
    public int targetuserid;
    public int reply;
    public int likes;
    public int dislike;
    public int forward;//转发
    public int collect;
    public int browse;
    public int funny;
    public int target;
    @TableField(exist = false)
    private String special;//更新单个数据，借助它来写
    @TableField(exist = false)
    private int detal;//更新单个数据，借助它来写
}
