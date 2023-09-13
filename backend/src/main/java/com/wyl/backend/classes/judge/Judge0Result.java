package com.wyl.backend.classes.judge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用来获取judge0的判断结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Judge0Result {
    private String stdout;
    private double time;
    private int memory;
    private String stderr;
    private String token;
    private String compile_output;
    private String message;
    private Judge0status status;
    //    可能会提示一些有关language_id的错误
    private List<String> language_id;
    //    预期输出
    private String expectOut;
}
