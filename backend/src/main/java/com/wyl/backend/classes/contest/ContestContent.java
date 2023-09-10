package com.wyl.backend.classes.contest;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    public int getJoinpeople() {
        return joinpeople;
    }

    public void setJoinpeople(int joinpeople) {
        this.joinpeople = joinpeople;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContestpassword() {
        return contestpassword;
    }

    public void setContestpassword(String contestpassword) {
        this.contestpassword = contestpassword;
    }

    public int getContestid() {
        return contestid;
    }

    public void setContestid(int contestid) {
        this.contestid = contestid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getContestname() {
        return contestname;
    }

    public void setContestname(String contestname) {
        this.contestname = contestname;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getContestformat() {
        return contestformat;
    }

    public void setContestformat(String contestformat) {
        this.contestformat = contestformat;
    }

    public String getContestlimit() {
        return contestlimit;
    }

    public void setContestlimit(String contestlimit) {
        this.contestlimit = contestlimit;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getBlockedlist() {
        return blockedlist;
    }

    public void setBlockedlist(String blockedlist) {
        this.blockedlist = blockedlist;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
