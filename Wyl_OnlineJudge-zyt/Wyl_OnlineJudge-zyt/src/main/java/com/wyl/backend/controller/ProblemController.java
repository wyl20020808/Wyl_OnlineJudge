package com.wyl.backend.controller;

import com.wyl.backend.entity.problem.ProblemContent;
import com.wyl.backend.entity.problem.ProblemSample;
import com.wyl.backend.mapper.ProblemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequestMapping(value = "/problem")
@RestController
public class ProblemController {
    @Autowired //注入，一定要写
    private ProblemMapper problemMapper;

    @GetMapping("/query")
    public List<ProblemContent> query() {
        List<ProblemContent> queryResult = problemMapper.selectAll();
        return queryResult;
    }

    @GetMapping("/query/{id}")
    public ProblemContent query2(@PathVariable int id) {
        ProblemContent queryResult = problemMapper.selectById(id);
        System.out.println(queryResult.getTitle());
        return queryResult;
    }

    @GetMapping("/query/problemsample/{id}")
    public List<ProblemSample> query3(@PathVariable int id) {
        List<ProblemSample> queryResult = problemMapper.selectSampleById(id);
        System.out.println(queryResult);
        return queryResult;
    }

    @PostMapping("/insert")
    public String insertCommon(@RequestBody ProblemContent problemContent) {
        if (problemContent.getTitle() == "")
            return "必须输入题目名称!";
        List<ProblemContent> queryResult = problemMapper.selectAll();
        int problemid = queryResult.get(queryResult.size() - 1).getProblemid() + 1;//计算一下当前要插入的problemid
        //找出来样例，单独存放，因为样例可能会很多
        List<ProblemSample> problemsample = problemContent.getProblemsample();
        for (int i = 0; i < problemsample.size(); i++) {
            problemsample.get(i).setProblemid(problemid);
        }
        int cnt = 0;
        for (int i = 0; i < problemsample.size(); i++) {
            cnt += problemMapper.insertToSample(problemsample.get(i));
        }
        cnt += problemMapper.insert(problemContent);
        if (cnt > problemsample.size()) return "success";
        else return "error";
    }


}