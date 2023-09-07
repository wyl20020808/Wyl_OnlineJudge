package com.wyl.backend.classes.problem;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
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
    @TableField(select = false)
    private List<ProblemSample> problemsample;//样例输入输出
    private int submitcount;//提交次数
    private int aceptedcount;//通过次数
    private String difficulty;//难度
    private String havedata = "false";//是否有数据
    @TableField(select = false)
    private String special;//更新单个数据，借助它来写

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getHavedata() {
        return havedata;
    }

    public void setHavedata(String havedata) {
        this.havedata = havedata;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setAceptedcount(int aceptedcount) {
        this.aceptedcount = aceptedcount;
    }
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

    public List<ProblemSample> getProblemsample() {
        return problemsample;
    }

    public void setProblemsample(List<ProblemSample> problem_sample) {
        this.problemsample = problem_sample;
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
