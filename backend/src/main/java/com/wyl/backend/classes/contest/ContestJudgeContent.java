package com.wyl.backend.classes.contest;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("contestjudgecontent")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContestJudgeContent {
    @TableId("id")
    public int id;
    public int judgeid;
    public String judgestate;//
    public double runtime;//
    public String memory;//
    public int contestid;
   public String problemchar;
   public String submittime;
   public int userid;
   public int problemid;
}
