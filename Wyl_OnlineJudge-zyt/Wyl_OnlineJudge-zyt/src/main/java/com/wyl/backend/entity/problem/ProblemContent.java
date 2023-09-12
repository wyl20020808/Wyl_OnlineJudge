package com.wyl.backend.entity.problem;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("problemcontent")
public class ProblemContent implements Serializable {
    @TableId
    private int problemid;//题目id
    private String title;//标题
    private String description;//题目描述
    private String background;//题目背景
    private String inputformat;//输入格式
    private String outputformat;//输出格式
    private String hint;//提示
    private String algorithm;
    private String source;
    private int timelimit;//时间限制
    private int memorylimit;//内存限制
    private List<ProblemSample> problemsample;//样例输入输出
    private int submitcount;//提交次数
    private int aceptedcount;//通过次数
    private String difficulty;//难度
    @TableLogic(value="0",delval="1")
    private int deleted;
}
