package com.wyl.backend.service;

import com.wyl.backend.entity.judge.*;

import java.util.List;

public interface JudgeService {
    /**
     * 向judge0发送单个实例，并获取判题结果
     * @param push
     * @return
     */
    Judge0Result judgeOne(Submission push);
    /**
     * 通过token查看一个判题结果
     * @param token
     * @return
     */
    Judge0Result getOneJuge0ResultByToken(String token);

    /**
     * 向judge0发送多个实例并获取判题结果结果
     *
     * @return
     */
    List<Judge0Token> judgeMul(Submissions submissions);

    /**
     * 通过List<token>查看一组判题结果
     * @param tokens
     * @return
     */
    Judge0MulResult getMulJuge0ResultByToken(List<Judge0Token> tokens);

    /**
     * 根据题目id和用户提供的源码进行判题
     * @param problemId
     * @param source
     * @return
     */
    JudgeResultToUser judgeProblem(long problemId, String source, int language_id);
}
