package com.wyl.backend.entity.problem;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Problem implements Serializable {
    @TableId
    private long problem_id;
    private long create_man_id;
    private Timestamp create_time;
    private Timestamp update_time;
    private long update_man_id;
    private ProblemContent problem_content;
    @TableLogic(value="0",delval="1")
    private int deleted;
}
