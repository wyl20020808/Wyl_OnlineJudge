package com.wyl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyl.backend.entity.judge.*;
import com.wyl.backend.entity.problem.ProblemContent;
import com.wyl.backend.mapper.ProblemMapper;
import com.wyl.backend.mapper.SampleMapper;
import com.wyl.backend.properties.Judge0Properties;
import com.wyl.backend.service.JudgeService;
import com.wyl.backend.utils.Base64Util;
import com.wyl.backend.utils.HttpClientUtil;
import com.wyl.backend.utils.JsonUtil;
import com.wyl.backend.utils.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class JudgeServiceImpl implements JudgeService {
    @Autowired
    Judge0Properties judge0Properties;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    SampleMapper sampleMapper;
    @Autowired
    ProblemMapper problemMapper;
    @Autowired
    OssUtil ossUtil;

    /**
     * 调用Http接口发送单个程序信息给Judge0
     * Judge0判题结束后返回运行结果
     *
     * @param push
     * @return
     */
    @Override
    public Judge0Result judgeOne(Submission push) {
        Map<String, Object> postBody = JsonUtil.convertObjectToMap(push);
        String result = null;
        try {
            result = String.valueOf(HttpClientUtil.doPostJson(judge0Properties.PUSH_ONE_SUBMISSION_API, postBody));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Judge0Result judge0Result = null;
        try {
            judge0Result = JsonUtil.jsonToObject(result, Judge0Result.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return judge0Result;
    }


    /**
     * 调用Http接口发送一个toke给Judge0
     * Judge0服务器收到后返回单个程序运行结果
     *
     * @param token
     * @return
     */
    @Override
    public Judge0Result getOneJuge0ResultByToken(String token) {
        String url = judge0Properties.GET_API + token;
        String res = HttpClientUtil.doGet(url, null);
        Judge0Result result = null;
        try {
            result = JsonUtil.jsonToObject(res, Judge0Result.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 同时判断多个实例，并返回token列表
     *
     * @param submissions
     * @return
     */
    @Override
    public List<Judge0Token> judgeMul(Submissions submissions) {
        Map<String, Object> map = JsonUtil.convertObjectToMap(submissions);
        String res = null;
        List<Judge0Token> judge0Tokens;
        try {
            res = String.valueOf(HttpClientUtil.doPostJson(judge0Properties.PUSH_MUL_SUBMISSION_API, map));
        } catch (IOException e) {
            log.info("judge0接口访问失败=>{}", judge0Properties.PUSH_MUL_SUBMISSION_API);
            throw new RuntimeException(e);
        }
        try {
            judge0Tokens = objectMapper.readValue(res, new TypeReference<List<Judge0Token>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return judge0Tokens;
    }

    /**
     * 同时获取多个判题结果
     *
     * @param tokens
     * @return
     */
    @Override
    public Judge0MulResult getMulJuge0ResultByToken(List<Judge0Token> tokens) {
        Judge0MulResult result = null;
        StringBuilder str_tokens = new StringBuilder();
        str_tokens.append(judge0Properties.GETS_MUL_RESULT_API);
        for (Judge0Token token : tokens) {
            str_tokens.append(token.getToken()).append(',');
        }
        String resJson = HttpClientUtil.doGet(String.valueOf(str_tokens), null);

        try {
            result = JsonUtil.jsonToObject(resJson, Judge0MulResult.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public JudgeResultToUser judgeProblem(long problemId, String source_code, int language_id) {

        //        题目的时间限制
        int timelimit;
        //        题目的空间限制
        int memorylimit;
        //        输入暂存
        String stdIn = null;
        //        输出暂存
        String stdOut = null;
        //        示例暂存
        Submissions submissions = null;
        //        打包后的全部的示例
        LinkedList<Submission> arrayList = new LinkedList<Submission>();
        //        用来存储每次调用/submissions/batch接口获取的判题查询结果
        Judge0MulResult judgeResults = null;
        //        用来存储已经判题完成的题目结果
        List<Judge0Result> finishedSubmissions = new LinkedList<>();
        //        判题状态，>2表示判题结束
        int statusId;
        //        用户通过的示例的数量
        int rightCount = 0;
        //        用户未通过的示例的数量
        int errCount = 0;
        //        用来存放预期输出，key为题目的token，val为预期输出
        Map<String, String> expectOuts = new HashMap<>();

        //将源码转为base64
        source_code = Base64Util.encode(source_code);

        //        获取题目限制（运行时间限制、地址空间限制）
        ProblemContent problemContent = getLimitByProblemId(problemId);
//        timelimit = problemContent.getTimelimit();
//        memorylimit = problemContent.getMemorylimit();
        timelimit = 2;
        memorylimit = 128000;

        //        根据题目id查询出题目全部样例的输入输出
        String path = "oj/" + problemId;
        List<InAndOut> inOutFromOss = ossUtil.getInOutFromOss(path);
//10
        //        将全部的样例打包成Submissions
        for (InAndOut s : inOutFromOss) {
            //从oss里面读取文件
            try {
                stdIn = String.valueOf(ossUtil.readFileFromOss(s.getIn()));
                stdOut = String.valueOf(ossUtil.readFileFromOss(s.getOut()));
                stdIn = Base64Util.encode(stdIn);
                stdOut = Base64Util.encode(stdOut);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //找空闲
            arrayList.add(new Submission(source_code, language_id, stdIn, stdOut, timelimit, memorylimit));
        }
        submissions = new Submissions(arrayList);
//
        //        发送Submissions至judge0并获取List<Token>
        List<Judge0Token> judge0Tokens = judgeMul(submissions);

        //        list是有顺序的，tokens是按照submission的顺序创建的，使用循环合并这两者,以便根据token获取测试点的预期输出
        List<Submission> submissionList = submissions.getSubmissions();
        for (int i = 0, j = 0; i < judge0Tokens.size() && j<submissionList.size() ;) {
            expectOuts.put(judge0Tokens.get(i++).getToken(),submissionList.get(j++).getExpected_output());
        }


        //等待判题完成
        sleep(500);

        //        查询判题结果
        while (!judge0Tokens.isEmpty()) {
            //        根据List<Token>查询判题结果
            judgeResults = getMulJuge0ResultByToken(judge0Tokens);

            //        从List<Token>中删除判题结果已经出来的token
            for (Judge0Result res : judgeResults.getSubmissions()) {
                statusId = res.getStatus().getId();

                if (statusId > 2) {//            取出已经出来的判题结果
                    finishedSubmissions.add(res);
                    judge0Tokens.removeIf(token -> Objects.equals(res.getToken(), token.getToken()));
                }

            }
            sleep(100);
        }

        //        统计答题情况并合并预期输出
        for (Judge0Result res : finishedSubmissions) {
            statusId = res.getStatus().getId();
            if (statusId == 3) {
                rightCount++;
            } else {
                errCount++;
            }
            //  设置预期输出
            res.setExpectOut(expectOuts.get(res.getToken()));
        }

        //        记录答题情况
        return new JudgeResultToUser(rightCount + errCount, rightCount, errCount, errCount == 0, finishedSubmissions);
    }

    /**
     * 以下是工具方法
     *
     */


    /**
     * 根据ProblemId 来获取问题的限制
     *
     * @param problemId
     * @return
     */
    private ProblemContent getLimitByProblemId(long problemId) {
        QueryWrapper<ProblemContent> lqw = new QueryWrapper<ProblemContent>();
        lqw.select("timelimit", "memorylimit");
        lqw.apply("problemid=" + problemId);
        return problemMapper.selectOne(lqw);
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
