package com.wyl.backend.classes.problem.controller;

import com.wyl.backend.classes.problem.Problem;
import com.wyl.backend.classes.problem.ProblemContent;
import com.wyl.backend.classes.problem.ProblemSample;
import com.wyl.backend.classes.problem.sql.CreateProblemContent;
import com.wyl.backend.classes.problem.sql.ProblemContentSQL;
import com.wyl.backend.classes.problem.sql.ProblemSampleSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RequestMapping(value = "/problem")
@RestController
public class ProblemControllerMapper {
    @Autowired //注入，一定要写
    private CreateProblemContent createProblemContent;
    @Autowired
    private ProblemContentSQL problemContentSQL;
    @Autowired
    private ProblemSampleSQL problemSampleSQL;
//    @PostMapping("/update/problemcontent")
    public String updateProblemContent(ProblemContent problemContent){
        problemContentSQL.updateById(problemContent);
        return "yes";
    }
//    @PostMapping("/update/problemcontent")
    public String updateProblemSample(List<ProblemSample> problemSample){
        problemSampleSQL.deleteById(problemSample.get(0).getProblemid());
        for (ProblemSample problemSample1 : problemSample){
            problemSampleSQL.insert(problemSample1);
        }

        return "yes";
    }

    @PostMapping("/update/problem")
    public String updateProblem(@RequestBody Problem problem){
        List<ProblemSample> problemSample = problem.getProblemsample();
        ProblemContent  problemContent = problem.getProblemcontent();
        updateProblemContent(problemContent);
        updateProblemSample(problemSample);
        return "yes";
    }

    @PostMapping("/delete/problem")
    public String deleteProblem(@RequestBody ProblemSample problem){
        problemContentSQL.deleteById(problem.getProblemid());
        problemSampleSQL.deleteById(problem.getProblemid());
        return "yes";
    }

    @GetMapping("/query")
    public List<ProblemContent> query() {
         List<ProblemContent> queryResult = createProblemContent.selectAll();
         return queryResult;
    }
    @GetMapping("/query/{id}")
    public ProblemContent query2(@PathVariable int id) {
        ProblemContent queryResult = createProblemContent.selectById(id);
        System.out.println(queryResult.getTitle());
        return queryResult;
    }
    @GetMapping("/query/problemsample/{id}")
    public List<ProblemSample> query3(@PathVariable int id) {
        List<ProblemSample>  queryResult = createProblemContent.selectSampleById(id);
        return queryResult;
    }

    @GetMapping("/query/probleminfo/{id}")
    public Problem query4(@PathVariable int id) {
        Problem queryResult = new Problem();
        queryResult.setProblemsample(createProblemContent.selectSampleById(id));
        queryResult.setProblemcontent(createProblemContent.selectById(id));
        return queryResult;
    }
    @PostMapping("/insert")
    public String insertCommon(@RequestBody ProblemContent problemContent){
        int cnt = 0;
        if(problemContent.getTitle() == "")
            return "必须输入题目名称!";
        cnt += createProblemContent.insert(problemContent);
        List<ProblemContent> queryResult = createProblemContent.selectAll();
        int problemid = queryResult.get(queryResult.size()-1).getProblemid() ;//计算一下当前要插入的problemid
        //找出来样例，单独存放，因为样例可能会很多
        List<ProblemSample>problemsample = problemContent.getProblemsample();
        for(int i = 0;i < problemsample.size();i++){
            problemsample.get(i).setProblemid(problemid);
        }

        for(int i = 0;i < problemsample.size();i++){
            cnt += createProblemContent.insertToSample(problemsample.get(i));
        }

        if(cnt > problemsample.size())return "success";
        else return "error";
    }
}