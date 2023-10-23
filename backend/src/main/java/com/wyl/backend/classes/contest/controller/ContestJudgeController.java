package com.wyl.backend.classes.contest.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wyl.backend.classes.contest.ContestJudge;
import com.wyl.backend.classes.contest.ContestJudgeContent;
import com.wyl.backend.classes.contest.SQL.ContestJudgeContentSQL;
import com.wyl.backend.classes.contest.SQL.ContestJudgeSQL;
import com.wyl.backend.classes.judge.Judge;
import com.wyl.backend.classes.judge.JudgeContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequestMapping(value = "/contest")
@RestController
public class ContestJudgeController {
    @Autowired
    private ContestJudgeSQL judgeSql;
    @Autowired
    private ContestJudgeContentSQL judgeContentSql;
    @PostMapping("/insert/judge")
    public int insertJudge(@RequestBody ContestJudge judgeInfo){
        try {
            judgeSql.insert(judgeInfo);
            judgeContentSql.updateJudgeId();
            judgeContentSql.updateJudgeProblemChar();
            return 1;
        }catch (Exception e){
            return -1;
        }
    }
    @PostMapping("/insert/judgecontent")
    public int insertJudgeContent(@RequestBody List<ContestJudgeContent> judgeInfo){
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
    @GetMapping("/query/userproblem")
    public List<ContestJudge> queryJudgeByUseridAndProblemid(@RequestParam int problemid, @RequestParam int userid,@RequestParam int contestid){
        QueryWrapper<ContestJudge> query = new QueryWrapper<>();
        query.eq("problemid",problemid).eq("userid",userid)
                .eq("contestid",contestid);

        return judgeSql.selectList(query);
    }
    @GetMapping("/query/judgebyid")
    public ContestJudge queryJudge(@RequestParam int judgeid){
        QueryWrapper<ContestJudge> query = new QueryWrapper<>();
        query.eq("judgeid",judgeid);    
        return judgeSql.selectOne(query);
    }

    @GetMapping("/query/judgecontent")
    public List<ContestJudgeContent> queryJudgeContent(@RequestParam int judgeid){
        QueryWrapper<ContestJudgeContent> query = new QueryWrapper<>();
        query.eq("judgeid",judgeid);
        return judgeContentSql.selectList(query);
    }
    @GetMapping("/query/alljudge")
    public List<ContestJudge> queryJudgeid(@RequestParam int contestid){
        QueryWrapper<ContestJudge> query = new QueryWrapper<>();
        query.eq("contestid",contestid);
        return judgeSql.selectList(query);
    }

    @PostMapping("/query/judgemany")//实时查询判题结果
    public List<ContestJudgeContent> QueryJudgeMany(int contestid,String submittime, int userid, int problemId){
        QueryWrapper<ContestJudgeContent> query = new QueryWrapper<>();
        query.eq("problemid",problemId).eq("userid",userid).eq("submittime",submittime)
                .eq("contestid",contestid);
        return judgeContentSql.selectList(query);
    }
}
