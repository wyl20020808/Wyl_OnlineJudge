package com.wyl.backend.classes.problem;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.List;
@TableName("problemcontent")
public class ProblemContent {
    private String title;//标题
    @TableId
    private int problemid;//题目id
    private String description;//题目描述
    private String background;//题目背景
    private String inputformat;//题目背景
    private String outputformat;//题目背景
    private String hint;//提示

    private int timelimit;//时间限制
    private int memorylimit;//内存限制
    @TableField(exist = false)
    private List<ProblemSample> problem_sample;//样例输入输出
    private int submitcount;//提交次数
    private int aceptedcount;//通过次数
    private String difficulty;//难度
    public int getTimelimit() {
        return timelimit;
    }

    public int getMemorylimit() {
        return memorylimit;
    }

    public int getSubmitcount() {
        return submitcount;
    }

    public int getAceptedcount() {
        return aceptedcount;
    }
    public void setTimelimit(int time_limit) {
        this.timelimit = time_limit;
    }

    public void setMemorylimit(int memory_limit) {
        this.memorylimit = memory_limit;
    }

    public void setSubmitcount(int submit_count) {
        this.submitcount = submit_count;
    }

    public void setAcepted_count(int acepted_count) {
        this.aceptedcount = acepted_count;
    }



    public int getProblemid() {
        return problemid;
    }

    public void setProblemid(int problem_id) {
        this.problemid = problem_id;
    }

    public String getInputformat() {
        return inputformat;
    }

    public void setInputformat(String inputformat) {
        this.inputformat = inputformat;
    }

    public String getOutputformat() {
        return outputformat;
    }

    public void setOutputformat(String outputformat) {
        this.outputformat = outputformat;
    }

    public List<ProblemSample> getProblem_sample() {
        return problem_sample;
    }

    public void setProblem_sample(List<ProblemSample> problem_sample) {
        this.problem_sample = problem_sample;
    }

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





    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }


}
