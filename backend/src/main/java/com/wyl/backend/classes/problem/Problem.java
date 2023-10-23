package com.wyl.backend.classes.problem;


import java.util.List;

public class Problem {
    private String createman;
    private String createtime;
    private String updatetime;
    private String updateman;
    private String problemid;
    private ProblemContent problemcontent;
    private List<ProblemSample> problemsample;

    public List<ProblemSample> getProblemsample() {
        return problemsample;
    }

    public void setProblemsample(List<ProblemSample> problemsample) {
        this.problemsample = problemsample;
    }

    public String getCreateman() {
        return createman;
    }

    public void setCreateman(String createman) {
        this.createman = createman;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUpdateman() {
        return updateman;
    }

    public void setUpdateman(String updateman) {
        this.updateman = updateman;
    }

    public String getProblemid() {
        return problemid;
    }

    public void setProblemid(String problemid) {
        this.problemid = problemid;
    }

    public ProblemContent getProblemcontent() {
        return problemcontent;
    }

    public void setProblemcontent(ProblemContent problemcontent) {
        this.problemcontent = problemcontent;
    }
}
