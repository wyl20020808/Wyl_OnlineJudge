package com.wyl.backend.classes.service;

import com.wyl.backend.classes.judge.Judge0Result;
import com.wyl.backend.classes.judge.JudgeResultToUser;
import com.wyl.backend.classes.judge.Submission;

import java.util.Map;

public interface JudgeService {
    /**
     * 向judge0发送单个实例，并获取判题结果
     * @param push
     * @return
     */
//    String judgeOne(Submission push, Map<String, String> MachineToToken,Map<String, String> TokenToMachine);

    Judge0Result judgeOne(Submission push, Map<String, String> MachineToToken, Map<String, String> TokenToMachine);
    /**
     * 通过token查看一个判题结果
     * @param token
     * @return
     */
    Judge0Result getOneJuge0ResultByToken(String mechine,String token);

    /**
     * 向judge0发送多个实例并获取判题结果结果
     *
     * @return
     */


    /**
     * 通过List<token>查看一组判题结果
     * @param tokens
     * @return
     */


    /**
     * 根据题目id和用户提供的源码进行判题
     * @param problemId
     * @param source
     * @return
     */
    JudgeResultToUser judgeProblem(long problemId, String source, int language_id);
}
