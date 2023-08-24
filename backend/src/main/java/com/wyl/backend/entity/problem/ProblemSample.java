package com.wyl.backend.entity.problem;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class ProblemSample implements Serializable {
    @TableId
    private int problemid;
    private String input;
    private String output;

}
