package com.wyl.backend.keda.classes.student;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("studentinfo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentInfo {
    @TableId(value = "id")
    public int id;
    public String name;
    public String createtime;
    public String studentid;
    public int age;
    public String sex;
    @TableField(value = "class")
    public String classes;
    public String password;
    public String headmaster;
    @TableLogic // 标记逻辑删除注解
    private Boolean deleted = false;
}
