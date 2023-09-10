package com.wyl.backend.classes.contest;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@TableName("contestjudgecontent")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContestJudgeContent {
    @TableId("id")
    public int id;
    public int judgeid;
    public String judgestate;//
    public int runtime;//
    public String memory;//

    public int contestid;

   public String problemchar;

    public String getProblemchar() {
        return problemchar;
    }

    public void setProblemchar(String problemchar) {
        this.problemchar = problemchar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getContestid() {
        return contestid;
    }

    public void setContestid(int contestid) {
        this.contestid = contestid;
    }
}
