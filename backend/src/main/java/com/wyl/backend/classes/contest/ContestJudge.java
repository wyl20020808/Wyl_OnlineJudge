package com.wyl.backend.classes.contest;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("contestjudge")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContestJudge {
    @TableId(value = "judgeid")
    public int judgeid;
    public String judgestate;
    public int problemid;
    public int userid;
    public int runtime;
    public String memory;
    public String language;
    public String submittime;
    public String problemname;
    public String username;
    public String code;
    public String compileoutput;
    public int totaltime;
    public int score;
    public int contestid;
    private String problemchar;
}
