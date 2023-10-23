package com.wyl.backend.classes.problem;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
@Data
@TableName("problemcontent")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProblemContent {
    private String title;//标题
    @TableId(value = "problemid")
    private int problemid;//题目id
    private String description;//题目描述
    private String background;//题目背景
    private String inputformat;//输入格式
    private String outputformat;//输出格式
    private String hint;//提示
    private String algorithm;//
    private String source;
    private int timelimit;//时间限制
    private int memorylimit;//内存限制
    private String questionbank;
    @TableField(exist = false )
    private List<ProblemSample> problemsample;//样例输入输出
    private int submitcount;//提交次数
    private int aceptedcount;//通过次数
    private String difficulty;//难度
    private int datalength;//是否有数据
    @TableField(exist = false)
    private String special;//更新单个数据，借助它来写
    
}
