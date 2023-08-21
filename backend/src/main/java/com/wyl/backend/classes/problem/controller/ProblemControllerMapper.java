package com.wyl.backend.classes.problem.controller;

import com.wyl.backend.classes.problem.ProblemContent;
import com.wyl.backend.classes.problem.sql.CreateProblem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RequestMapping(value = "/problem")
@RestController
public class ProblemControllerMapper {
    @Autowired //注入，一定要写
    private CreateProblem  createProblem;
    @GetMapping("/query")
    public List<ProblemContent> query() {
         List<ProblemContent> queryResult = createProblem.selectList(null);
        System.out.println(queryResult.get(0).getTimelimit());
         return queryResult;
    }
    @PostMapping("/insert")
    public String insertCommon(ProblemContent problemContent){
        int i =  createProblem.insert(problemContent);
        if(i > 0)return "yes";
        else return "insert success";
    }
}
