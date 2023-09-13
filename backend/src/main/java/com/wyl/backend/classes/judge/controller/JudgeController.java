package com.wyl.backend.classes.judge.controller;

import com.alibaba.druid.sql.parser.Lexer;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wyl.backend.classes.judge.Judge;
import com.wyl.backend.classes.judge.JudgeContent;
import com.wyl.backend.classes.judge.JudgeResultToUser;
import com.wyl.backend.classes.judge.sql.JudgeContentSQL;
import com.wyl.backend.classes.judge.sql.JudgeSql;
import com.wyl.backend.classes.service.JudgeService;
import com.wyl.backend.classes.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@CrossOrigin
@RequestMapping(value = "/judge")
@RestController
public class JudgeController {
    @Autowired
    private JudgeSql judgeSql;
    @Autowired
    private JudgeContentSQL judgeContentSql;
    @Autowired
    JudgeService judgeService;

    @PostMapping("/insert/judge")
    public int insertJudge(@RequestBody Judge judgeInfo){
        try {
            judgeSql.insert(judgeInfo);
            List<Judge> query = judgeSql.selectList(null);
            return query.get(query.size()-1).getJudgeid();
        }catch (Exception e){
            return -1;
        }
    }
    @PostMapping("/insert/judgecontent")
    public int insertJudgeContent(@RequestBody List<JudgeContent> judgeInfo){
        try{

            for(int i = 0; i < judgeInfo.size(); i++)
                judgeContentSql.insert(judgeInfo.get(i));
            return 1;
        }catch (Exception e){
            System.out.println("wrong wrong wrong  wrong wrong wrong" + e.getMessage());
            System.out.println();
            return -1;
        }
    }

    @GetMapping("/query/judgebyid")
    public Judge queryJudge(@RequestParam int judgeid){
        QueryWrapper<Judge> query = new QueryWrapper<>();
        query.eq("judgeid",judgeid);
        return judgeSql.selectOne(query);
    }

    @GetMapping("/query/judgecontent")
    public List<JudgeContent> queryJudgeContent(@RequestParam int judgeid){
        QueryWrapper<JudgeContent> query = new QueryWrapper<>();
        query.eq("judgeid",judgeid);
        return judgeContentSql.selectList(query);
    }
    @GetMapping("/query/alljudge")
    public List<Judge> queryJudgeid(){
        return judgeSql.selectList(null);
    }

    @PostMapping("/judgeForm")
    public Result<JudgeResultToUser> pushMulToJudge0Form(long problemId, String source_code, int languageId){
        JudgeResultToUser judgeResultToUser = judgeService.judgeProblem(problemId, source_code, languageId);

        log.info("用户判题成功=>{}",problemId);
        return Result.success(judgeResultToUser);
    }
}
