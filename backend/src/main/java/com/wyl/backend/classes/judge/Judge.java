package com.wyl.backend.classes.judge;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@TableName("judge")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Judge {
    @TableId(value = "judgeid")
    public int judgeid;
    public String judgestate;
    public int problemid;
    public int userid;
    public int runtime;
    public String memory;
    public String language;
    public String submittime;

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

    public int getProblemid() {
        return problemid;
    }

    public void setProblemid(int problemid) {
        this.problemid = problemid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSubmittime() {
        return submittime;
    }

    public void setSubmittime(String submittime) {
        this.submittime = submittime;
    }
}
