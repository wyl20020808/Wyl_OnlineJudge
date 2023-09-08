package com.wyl.backend.classes.contest;

import java.util.List;

public class Contest {
    private ContestContent contestcontent;
    private List<ContestAdmin> contestadmin;

    private List<ContestProblem> contestproblem;

    public ContestContent getContestcontent() {
        return contestcontent;
    }

    public void setContestcontent(ContestContent contestcontent) {
        this.contestcontent = contestcontent;
    }

    public List<ContestAdmin> getContestadmin() {
        return contestadmin;
    }

    public void setContestadmin(List<ContestAdmin> contestadmin) {
        this.contestadmin = contestadmin;
    }

    public List<ContestProblem> getContestproblem() {
        return contestproblem;
    }

    public void setContestproblem(List<ContestProblem> contestproblem) {
        this.contestproblem = contestproblem;
    }
}
