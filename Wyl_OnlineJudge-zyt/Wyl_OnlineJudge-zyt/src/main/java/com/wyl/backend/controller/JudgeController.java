package com.wyl.backend.controller;

import com.wyl.backend.entity.judge.Judge0Result;
import com.wyl.backend.entity.judge.JudgeResultToUser;
import com.wyl.backend.entity.judge.Submission;
import com.wyl.backend.entity.utils.Result;
import com.wyl.backend.entity.judge.UserPushJudge;
import com.wyl.backend.service.JudgeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 判题接口
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/judge")
public class JudgeController {
    @Autowired
    JudgeService judgeService;

    /**
     * 编译程序，并返回编译结果
     * 返回用户自己提交的实例测试结果
     * @param push 实例信息
     * @return  判题结果
     * @throws Exception
     */
    @PostMapping("/judgeOne")
    public Result<Judge0Result> pushOneToJudge0(@RequestBody Submission push) throws Exception {
        log.info("新增判题=>{}", push.toString());
        Judge0Result result = judgeService.judgeOne(push);
        return Result.success(result);
    }

    /**
     * 通过token查询程序编译结果
     * @param token 实例对应的唯一标识码
     * @return  判题结果
     */
    @GetMapping("/getOneResult/{token}")
    public Result<Judge0Result> getOneJudge0Result(@PathVariable String token){
        log.info("查询题目-token:{}",token);
        Judge0Result result = judgeService.getOneJuge0ResultByToken(token);
        return Result.success(result);
    }

    /**
     * 根据用户传递过来的problemId和languageId和sourceCode判题
     * @param pushJudge 用户提供的判题信息
     * @return 判题结果
     */
    @PostMapping("/judge")
    public Result<JudgeResultToUser> pushMulToJudge0(@RequestBody UserPushJudge pushJudge){
        JudgeResultToUser judgeResultToUser = judgeService.judgeProblem(pushJudge.getProblemId(), pushJudge.getSource_code(), pushJudge.getLanguage_id());

        log.info("用户判题成功=>{}",pushJudge.getProblemId());
        return Result.success(judgeResultToUser);
    }

    /**
     * 使用表单的形式创建的api
     * 方便测试
     * @param problemId 题目id
     * @param source_code 源码
     * @param languageId 语言的id
     * @return 判题结果
     */
    @PostMapping("/judgeForm")
    public Result<JudgeResultToUser> pushMulToJudge0Form(long problemId,String source_code,int languageId){
        JudgeResultToUser judgeResultToUser = judgeService.judgeProblem(problemId, source_code, languageId);

        log.info("用户判题成功=>{}",problemId);
        return Result.success(judgeResultToUser);
    }

}
