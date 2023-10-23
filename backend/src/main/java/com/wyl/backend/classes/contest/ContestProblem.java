package com.wyl.backend.classes.contest;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@TableName("contestproblem")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContestProblem {
    @TableId(value = "id")
    private int id;
    private int contestid;
    private int problemid;
    private String problemname;

    private int submitcount;
    private int acceptedcount;
   private String problemchar;

    public String getProblemchar() {
        return problemchar;
    }

    public void setProblemchar(String problemchar) {
        this.problemchar = problemchar;
    }

    public int getSubmitcount() {
        return submitcount;
    }

    public void setSubmitcount(int submitcount) {
        this.submitcount = submitcount;
    }

    public int getAcceptedcount() {
        return acceptedcount;
    }

    public void setAcceptedcount(int acceptedcount) {
        this.acceptedcount = acceptedcount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContestid() {
        return contestid;
    }

    public void setContestid(int contestid) {
        this.contestid = contestid;
    }

    public int getProblemid() {
        return problemid;
    }

    public void setProblemid(int problemid) {
        this.problemid = problemid;
    }

    public String getProblemname() {
        return problemname;
    }

    public void setProblemname(String problemname) {
        this.problemname = problemname;
    }
}
