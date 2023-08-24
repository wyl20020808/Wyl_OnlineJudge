package com.wyl.backend.entity.problem;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Problem implements Serializable {
    @TableId
    private String problem_id;
    private String create_man;
    private String create_time;
    private String update_time;
    private String update_man;
    private ProblemContent problem_content;
}
