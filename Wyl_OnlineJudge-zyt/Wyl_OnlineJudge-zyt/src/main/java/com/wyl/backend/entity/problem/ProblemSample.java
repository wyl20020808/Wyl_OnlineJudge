package com.wyl.backend.entity.problem;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("problemsample")
public class ProblemSample implements Serializable {
    @TableId
    private int problemid;
    private String input;
    private String output;
    private String path;
    @TableLogic(value="0",delval="1")
    private int deleted;
}
