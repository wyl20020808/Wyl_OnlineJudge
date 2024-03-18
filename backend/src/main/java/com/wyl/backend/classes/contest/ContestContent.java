package com.wyl.backend.classes.contest;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("contestcontent")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContestContent {
    @TableId(value = "contestid")
    private int contestid;
    private int userid;
    private String username;
    private String contestname;
    private String startdate;
    private String enddate;
    private String contestformat;
    private String contestlimit;
    private String rated;

    private String blockedlist;

    private  String createtime;

    private String contestpassword;

    private int joinpeople;

    private  String description;
    private int grade;
}
