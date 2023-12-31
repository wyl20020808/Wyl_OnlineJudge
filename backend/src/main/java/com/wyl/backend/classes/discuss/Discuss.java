package com.wyl.backend.classes.discuss;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
    public String discusstitle;//存一下依赖的是哪个title
    public int discussid;//存一下依赖的是哪个id
    public int targetuserid;
    public int reply;
    public int likes;
    public int dislike;
    public int forward;//转发
    public int collect;
    public int browse;
    public int funny;
    public int target;
    @TableLogic // 标记逻辑删除注解
    private Boolean deleted;
    @TableField(exist = false)
    private String special;//更新单个数据，借助它来写
    @TableField(exist = false)
    private int detal;//更新单个数据，借助它来写
}
