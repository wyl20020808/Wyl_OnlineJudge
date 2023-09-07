package com.wyl.backend.classes.contest;

import java.util.List;

public class Contest {
    private ContestContent contenstcontent;
    private List<ContestAdmin> contenstadmin;

    private List<ContestProblem> contestproblem;

    public ContestContent getContenstcontent() {
        return contenstcontent;
    }

    public void setContenstcontent(ContestContent contenstcontent) {
        this.contenstcontent = contenstcontent;
    }

    public List<ContestAdmin> getContenstadmin() {
        return contenstadmin;
    }

    public void setContenstadmin(List<ContestAdmin> contenstadmin) {
        this.contenstadmin = contenstadmin;
    }

    public List<ContestProblem> getContestproblem() {
        return contestproblem;
    }

    public void setContestproblem(List<ContestProblem> contestproblem) {
        this.contestproblem = contestproblem;
    }
}
