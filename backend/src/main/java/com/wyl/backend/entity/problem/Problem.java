package com.wyl.backend.entity.problem;

import lombok.Data;

@Data
public class Problem {
    private String create_man;
    private String create_time;
    private String update_time;
    private String update_man;
    private String problem_id;
    private ProblemContent problem_content;
}
