package com.wyl.backend.classes.problem;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.List;
@TableName("problemcontent")
public class ProblemContent {
    private String title;//标题
    @TableId
    private int problem_id;//题目id
    private String description;//题目描述
    private String background;//题目背景
    private String inputformat;//题目背景
    private String outputformat;//题目背景
    private String hint;//提示
    private String time_limit;//时间限制
    private String memory_limit;//内存限制
    @TableField(exist = false)
    private List<ProblemSample> problem_sample;//样例输入输出

    private String difficulty;//难度
    private String submit_count;//提交次数
    private String acepted_count;//通过次数

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(String time_limit) {
        this.time_limit = time_limit;
    }

    public String getMemory_limit() {
        return memory_limit;
    }

    public void setMemory_limit(String memory_limit) {
        this.memory_limit = memory_limit;
    }



    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getSubmit_count() {
        return submit_count;
    }

    public void setSubmit_count(String submit_count) {
        this.submit_count = submit_count;
    }

    public String getAcepted_count() {
        return acepted_count;
    }

    public void setAcepted_count(String acepted_count) {
        this.acepted_count = acepted_count;
    }
}
