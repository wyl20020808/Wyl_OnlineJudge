package com.wyl.backend.classes.contest;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@TableName("contestant")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contestant {
    @TableId(value = "id")
    private int id;

    private int contestid;

    private int userid;

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

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
