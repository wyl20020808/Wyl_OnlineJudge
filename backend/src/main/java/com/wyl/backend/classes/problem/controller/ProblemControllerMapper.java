package com.wyl.backend.classes.problem.controller;

import com.wyl.backend.classes.problem.ProblemContent;
import com.wyl.backend.classes.problem.sql.CreateProblem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RequestMapping(value = "/problem")
@RestController
public class ProblemControllerMapper {
    @Autowired //注入，一定要写
    private CreateProblem  createProblem;
    @GetMapping("/query")
    public List<ProblemContent> query() {
         List<ProblemContent> queryResult = createProblem.selectList(null);
//        System.out.println(queryResult.get(4).getTimelimit());
         return queryResult;
    }
    @PostMapping("/insert")
    public String insertCommon(@RequestBody ProblemContent problemContent){
        if(problemContent.getTitle() == "")
            return "必须输入题目名称!";
        int i =  createProblem.insert(problemContent);
        if(i > 0)return "success";
        else return "error";
    }
}
