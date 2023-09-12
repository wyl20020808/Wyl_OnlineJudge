package com.wyl.backend.entity.judge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 向用户传递判题结果的实体类
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JudgeResultToUser {
    //    总测试样例数量
    private int testCount;
    //    总通过数量
    private int passCount;
    //    错误样例数量
    private int errCount;
//    //    错误答案数量
//    private int wrongAnswerCount;
//    //    超时样例数量
//    private int timeLimitExceededCount;
//    //    运行时错误数量
//    private int runtimeErrorCount;
//    //    是否编译出错
//    private boolean isCompilationError;
    //    是否通过
    private boolean isPassed;

    List<Judge0Result> results;
}
