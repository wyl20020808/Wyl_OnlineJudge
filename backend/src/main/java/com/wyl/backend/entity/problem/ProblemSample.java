package com.wyl.backend.entity.problem;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ProblemSample {
    @TableId
    private int problemid;
    private String input;
    private String output;

}
