package com.wyl.backend.classes.judge;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@TableName("judgecontent")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JudgeContent {
    @TableId("judgecontentid")
    public int judgecontentid;
    public int judgeid;
    public String judgestate;//
    public int runtime;//
    public String memory;//

    public int getJudgecontentId() {
        return judgecontentid;
    }

    public void setJudgecontentId(int judgecontentId) {
        this.judgecontentid = judgecontentId;
    }

    public int getJudgeid() {
        return judgeid;
    }

    public void setJudgeid(int judgeid) {
        this.judgeid = judgeid;
    }

    public String getJudgestate() {
        return judgestate;
    }

    public void setJudgestate(String judgestate) {
        this.judgestate = judgestate;
    }



    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }





}
