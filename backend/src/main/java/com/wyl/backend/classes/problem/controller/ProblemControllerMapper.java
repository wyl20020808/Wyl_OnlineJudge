package com.wyl.backend.classes.problem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wyl.backend.classes.problem.Problem;
import com.wyl.backend.classes.problem.ProblemCode;
import com.wyl.backend.classes.problem.ProblemContent;
import com.wyl.backend.classes.problem.ProblemSample;
import com.wyl.backend.classes.problem.sql.CreateProblemContent;
import com.wyl.backend.classes.problem.sql.ProblemCodeSQL;
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
    @Autowired
    private ProblemCodeSQL   problemCodeSQL;

    //2023年10月9日20:49:11
    @PostMapping("/update/problemcode")
    public void  updateProblemCode(@RequestBody ProblemCode problemCode) {
    //在这个例子中，updateById方法会尝试更新记录，如果更新成功，rows将会是更新的记录数，
        // 否则rows将会是0。如果rows是0，那么就执行insert方法来插入记录。
        //注意，这个操作并不是原子的，如果有其他线程在这两个操作之间修改了数据，
        // 可能会导致问题。如果你需要原子性操作，你可能需要在数据库层面实现upsert操作
        // ，这通常需要使用特定数据库的SQL语法。
        UpdateWrapper<ProblemCode> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userid",problemCode.getUserid()).eq("problemid", problemCode.getProblemid());
        int rows = problemCodeSQL.update(problemCode, updateWrapper);
        if (rows == 0) {
            problemCodeSQL.insert(problemCode);
        }
    }

    @GetMapping("/query/problemcode")
    public ProblemCode  queryProblemCode(@RequestParam("userid") int userid,
                                         @RequestParam("problemid") int problemid) {
        QueryWrapper<ProblemCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid",userid).eq("problemid",problemid);
        return  problemCodeSQL.selectOne(queryWrapper);
    }

    //    @PostMapping("/update/problemcontent")
    public String updateProblemContent(ProblemContent problemContent) {//更新单个题目所有信息，根据id
        problemContentSQL.updateById(problemContent);
        return "yes";
    }

    //    @PostMapping("/update/problemcontent")
    public String updateProblemSample(List<ProblemSample> problemSample) {
        problemSampleSQL.deleteById(problemSample.get(0).getProblemid());
        for (ProblemSample problemSample1 : problemSample) {
            problemSampleSQL.insert(problemSample1);
        }
        return "yes";
    }

    @PostMapping("/update/problemcontent/special")
    public void updateProblemContentSpecific(@RequestBody ProblemContent problemContent
                                             ) {

        try {
            if("submitcount".equals(problemContent.getSpecial()))
                problemContentSQL.incrementSubmitCount(problemContent.getProblemid());
            else if("aceptedcount".equals(problemContent.getSpecial()))
                problemContentSQL.incrementAceptedcountCount(problemContent.getProblemid());
        }catch(Exception e) {

        }


    }

    @PostMapping("/update/problemcontent")
    public String updateProblemContents(@RequestBody ProblemContent problemContent) {
        //直接全部更新题目信息，懒得分段更新了
        updateProblemContent(problemContent);
        return "yes";
    }

    @PostMapping("/update/problem")
    public String updateProblem(@RequestBody Problem problem) {//更新题目和样例，更新方式是直接全部更新和
        // 先删除原来的样例，再插入新的样例，最后更新题目
        List<ProblemSample> problemSample = problem.getProblemsample();
        ProblemContent problemContent = problem.getProblemcontent();
        updateProblemContent(problemContent);
        updateProblemSample(problemSample);
        return "yes";
    }

    @PostMapping("/delete/problem")
    public String deleteProblem(@RequestBody ProblemSample problem) {
        problemContentSQL.deleteById(problem.getProblemid());
        problemSampleSQL.deleteById(problem.getProblemid());
        return "yes";
    }

    @GetMapping("/query")
    public List<ProblemContent> query(@RequestParam(value = "questionbank") String questionBank) {
        QueryWrapper<ProblemContent> wrapper = new QueryWrapper<>();
        wrapper.eq("questionbank", questionBank);
        return problemContentSQL.selectList(wrapper);
    }

    @GetMapping("/query/{id}")
    public ProblemContent query2(@PathVariable int id) {
        return createProblemContent.selectById(id);
    }

    @GetMapping("/query/problemsample/{id}")
    public List<ProblemSample> query3(@PathVariable int id) {
        List<ProblemSample> queryResult = createProblemContent.selectSampleById(id);
        return queryResult;
    }

    @GetMapping("/query/probleminfo/{id}")
    public Problem query4(@PathVariable int id) {//查询题目和样例
        Problem queryResult = new Problem();
        queryResult.setProblemsample(createProblemContent.selectSampleById(id));
        queryResult.setProblemcontent(createProblemContent.selectById(id));
        return queryResult;
    }

    @PostMapping("/insert")
    public String insertCommon(@RequestBody ProblemContent problemContent) {
        int cnt = 0;
        if (problemContent.getTitle() == "")
            return "必须输入题目名称!";
        cnt += problemContentSQL.insert(problemContent);
        List<ProblemContent> queryResult = createProblemContent.selectAll();
        int problemid = queryResult.get(queryResult.size() - 1).getProblemid();//计算一下当前要插入的problemid
        //找出来样例，单独存放，因为样例可能会很多
        List<ProblemSample> problemsample = problemContent.getProblemsample();
        for (int i = 0; i < problemsample.size(); i++) {
            problemsample.get(i).setProblemid(problemid);
        }

        for (int i = 0; i < problemsample.size(); i++) {
            cnt += createProblemContent.insertToSample(problemsample.get(i));
        }

        if (cnt > problemsample.size()) return "success";
        else return "error";
    }
}
