package com.wyl.backend.classes.problem;


public class Problem {
    private String create_man;
    private String create_time;
    private String update_time;
    private String update_man;
    private String problem_id;
    private ProblemContent problem_content;
    public String getCreate_man() {
        return create_man;
    }

    public void setCreate_man(String create_man) {
        this.create_man = create_man;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getUpdate_man() {
        return update_man;
    }

    public void setUpdate_man(String update_man) {
        this.update_man = update_man;
    }

    public String getProblem_id() {
        return problem_id;
    }

    public void setProblem_id(String problem_id) {
        this.problem_id = problem_id;
    }

    public ProblemContent getProblem_content() {
        return problem_content;
    }

    public void setProblem_content(ProblemContent problem_content) {
        this.problem_content = problem_content;
    }


}
