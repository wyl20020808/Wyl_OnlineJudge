package com.wyl.backend.classes.judge;

import lombok.Data;

import java.util.List;

/**
 * 用来获取多个实例的judge0的判断结果
 */
@Data
public class Judge0MulResult {
    List<Judge0Result> submissions;
}