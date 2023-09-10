package com.wyl.backend.classes.contest;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    public String getProblemchar() {
        return problemchar;
    }

    public void setProblemchar(String problemchar) {
        this.problemchar = problemchar;
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

    public String getProblemname() {
        return problemname;
    }

    public void setProblemname(String problemname) {
        this.problemname = problemname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompileoutput() {
        return compileoutput;
    }

    public void setCompileoutput(String compileoutput) {
        this.compileoutput = compileoutput;
    }

    public int getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(int totaltime) {
        this.totaltime = totaltime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getContestid() {
        return contestid;
    }

    public void setContestid(int contestid) {
        this.contestid = contestid;
    }
}
