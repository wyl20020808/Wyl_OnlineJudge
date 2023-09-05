package com.wyl.backend.classes.user.userinfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@TableName("userextra")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserExtra {
    private int userid;
    private int submitcount;
    private int aceptedcount;
    @TableField(select = false)
    private String special;

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getSubmitcount() {
        return submitcount;
    }

    public void setSubmitcount(int submitcount) {
        this.submitcount = submitcount;
    }

    public int getAceptedcount() {
        return aceptedcount;
    }

    public void setAceptedcount(int aceptedcount) {
        this.aceptedcount = aceptedcount;
    }
}
