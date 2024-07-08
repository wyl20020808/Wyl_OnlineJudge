package com.wyl.backend.keda.classes.auxiliary.api;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("userac")
@JsonIgnoreProperties(ignoreUnknown = true)
public  class student{
    int score;
    int pid;
    int uid;
    @TableField(value = "gettime")
    String getTime;

    @TableLogic // 逻辑删除标记
    private Boolean deleted = false;

    public student(int y,int i, int i1,String x) {
        this.uid = y;
        this.score = i;
        this.pid = i1;
        this.getTime = x;
    }
}
