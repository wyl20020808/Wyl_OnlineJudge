package com.wyl.backend.classes.contest.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wyl.backend.classes.auxiliary.SubstringSearch;
import com.wyl.backend.classes.contest.*;
import com.wyl.backend.classes.contest.SQL.*;
import com.wyl.backend.classes.problem.ProblemContent;
import com.wyl.backend.classes.problem.sql.ProblemContentSQL;
import com.wyl.backend.classes.user.sql.UserOperator;
import com.wyl.backend.classes.user.userinfo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RequestMapping(value = "/contest")
@RestController
public class ContestController {
    @Autowired
    private UserOperator userOperator;
    @Autowired
    private ProblemContentSQL problemContentSQL;
    @Autowired
    private ContestContentSQL contestContentSQL;

    @Autowired
    private ContestAdminSQL  contestAdminSQL;
    @Autowired
    private ContestProblemSQL   contestProblemSQL;
    @Autowired
    private ContestantSQL  contestantSQL;

    @Autowired
    private ContestJudgeContentSQL contestJudgeSQL;
    // 获取用户信息
    @PostMapping("/query/user")
    public List<UserInfo> queryUser(@RequestBody UserInfo info) {
        Map<Integer,UserInfo> getUserinfo = new HashMap<>();
        List<UserInfo> userinfo = userOperator.select();//查找出用户信息
        ArrayList<String> words = new ArrayList<>();
        for (UserInfo userInfo : userinfo) {//放入名称,用的是nickname
            getUserinfo.put(userInfo.getUserid(), userInfo);
            words.add(String.valueOf(userInfo.getUserid()) + " " + userInfo.getNickname());
        }
        SubstringSearch search = new SubstringSearch(words);
        List<String> results = search.search(info.getSpecial());
        List<UserInfo> Results = new ArrayList<>();
        for(String result1 : results){
            Results.add(getUserinfo.get(Integer.parseInt(result1.split(" ")[0])));
        }
        return Results;//返回查找到的结果
    }

    @PostMapping("/query/problem")
    public List<ProblemContent> queryProblem(@RequestBody ProblemContent info) {//暂定查询所有问题信息，查询的消耗会比较大，考虑后续优化，新建一个表优化
        Map<Integer,ProblemContent> getProblem = new HashMap<>();
        List<ProblemContent> problems = problemContentSQL.selectList(null);//查找出用户信息
        ArrayList<String> words = new ArrayList<>();
        //存在的问题：如果题目名称前面有很多空格，就要处理，目前简单处理了
        for (ProblemContent probleminfo: problems) {
            String title = probleminfo.getTitle();
            while(title.charAt(0) == ' ') title = title.substring(1);//去掉前置空格
            getProblem.put(probleminfo.getProblemid(), probleminfo);
            words.add(String.valueOf(probleminfo.getProblemid()) + " " + title);
        }
        SubstringSearch search = new SubstringSearch(words);
        List<ProblemContent> Results = new ArrayList<>();
        List<String> results = search.search(info.getSpecial());
        for (String word: results) {
            Results.add(getProblem.get(Integer.parseInt(word.split(" ")[0])));
        }
        return Results;//返回查找到的结果
    }


    @PostMapping("/create")//这里还处理了前端传过来的数据，获取了contestid和各自的名字
    public void createContest(@RequestBody Contest info){
        try{
            System.out.println(info.getContestadmin());
            System.out.println("info.getContestcontent()");
        }catch(Exception e){
            System.out.println(e);
        }
        info.getContestcontent().setUsername(String.valueOf(userOperator.selectById(info.getContestcontent().getUserid()).getUsername()));
        contestContentSQL.insert(info.getContestcontent());
        List<ContestContent> contenstcontent = contestContentSQL.selectList(null);
        int id = contenstcontent.get(contenstcontent.size() - 1).getContestid();
        for(ContestAdmin admin: info.getContestadmin()){
            admin.setContestid(id);;
            admin.setUsername(String.valueOf(userOperator.selectById(admin.getUserid()).getUsername()));
            contestAdminSQL.insert(admin);
        }
        for(ContestProblem  problem: info.getContestproblem()){
            problem.setProblemname(String.valueOf(problemContentSQL.selectById(problem.getProblemid()).getTitle()));
            problem.setContestid(id);
            contestProblemSQL.insert(problem);
        }
    }

    @GetMapping("/query")
    public List<ContestContent> queryContestContent(){
        System.out.println(System.getProperty("user.dir") + "ceshi");
        return contestContentSQL.selectList(null);
    }

    @PostMapping("/join/personal")
    public int joinContestPersonal(@RequestBody Contestant info){
        try{
            contestantSQL.insert(info);
            UpdateWrapper<ContestContent> updateWrapper = new UpdateWrapper<>();
            updateWrapper.setSql("joinpeople = joinpeople + 1").eq("contestid", info.getContestid());
            contestContentSQL.update(null, updateWrapper);
            return 1;
        }catch(Exception e){
            System.out.println(e);
            return 0;
        }
    }
    @PostMapping("/join/personal/cancel")
    public int joinContestPersonalCancel(@RequestBody Contestant info){
        try{
            //取消报名 -1
            UpdateWrapper<ContestContent> updateWrapper = new UpdateWrapper<>();
            updateWrapper.setSql("joinpeople = joinpeople - 1").eq("contestid", info.getContestid());
            contestContentSQL.update(null, updateWrapper);
            //删除报名信息
            QueryWrapper<Contestant> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userid", info.getUserid()).eq("contestid", info.getContestid());

            return contestantSQL.delete(queryWrapper);
        }catch(Exception e){
            System.out.println(e);
            return 0;
        }
    }

    @GetMapping("/query/join/personal")
    public List<Contestant> queryJoinPersonal(@RequestParam int userid){//返回其已经注册过的比赛
        QueryWrapper<Contestant> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid", userid);
        return contestantSQL.selectList(queryWrapper);
    }

    @GetMapping("/query/contest")
    public Contest queryContest(@RequestParam int contestid){
        Contest result = new Contest();
        result.setContestcontent(contestContentSQL.selectById(contestid));
        result.setContestadmin(contestAdminSQL.selectList(new QueryWrapper<ContestAdmin>().eq("contestid", contestid)));;
        result.setContestproblem(contestProblemSQL.selectList(new QueryWrapper<ContestProblem>().eq("contestid", contestid)));
        return result;
    }

}