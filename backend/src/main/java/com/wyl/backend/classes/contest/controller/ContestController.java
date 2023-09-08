package com.wyl.backend.classes.contest.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wyl.backend.classes.contest.*;
import com.wyl.backend.classes.contest.SQL.ContestAdminSQL;
import com.wyl.backend.classes.contest.SQL.ContestContentSQL;
import com.wyl.backend.classes.contest.SQL.ContestProblemSQL;
import com.wyl.backend.classes.contest.SQL.ContestantSQL;
import com.wyl.backend.classes.contest.auxiliaryclass.Trie;
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
    // 获取用户信息
    @PostMapping("/query/user")
    public Map<String, Integer> queryUser(@RequestBody UserInfo info) {
        Map<String, Integer> result = new HashMap<>();
        Map<String, Integer> map = new HashMap<>();
        List<UserInfo> userinfo = userOperator.select();//查找出用户信息
        ArrayList<String> words = new ArrayList<>();
        for (UserInfo userInfo : userinfo) {//放入名称,用的是nickname
            words.add(userInfo.getNickname() + " " + userInfo.getUserid());
            words.add(String.valueOf(userInfo.getUserid()) + " " + userInfo.getNickname());
            map.put(userInfo.getNickname() + " " + userInfo.getUserid(), userInfo.getUserid());
            map.put(String.valueOf(userInfo.getUserid()) + " " + userInfo.getNickname(), userInfo.getUserid());
        }
        Trie trie = new Trie(words);//用trie树来加速查找
        List<String> results = trie.findWordsWithPrefix(info.getSpecial());
        for(String result1 : results){
            result.put(result1, map.get(result1));
        }
        return result;//返回查找到的结果
    }

    @PostMapping("/query/problem")
    public Map<String, Integer> queryProblem(@RequestBody ProblemContent info) {//暂定查询所有问题信息，查询的消耗会比较大，考虑后续优化，新建一个表优化
        Map<String, Integer> result = new HashMap<>();
        Map<String, Integer> map = new HashMap<>();
        List<ProblemContent> problems = problemContentSQL.selectList(null);//查找出用户信息
        ArrayList<String> words = new ArrayList<>();
        //存在的问题：如果题目名称前面有很多空格，就要处理，目前处理了
        for (ProblemContent probleminfo: problems) {//放入名称,用的是nickname
            System.out.println(probleminfo.getTitle() + " " + probleminfo.getProblemid() + "abcdefg");
            String title = probleminfo.getTitle();
            while(title.charAt(0) == ' ') title = title.substring(1);//去掉前置空格
            words.add(title + " " + probleminfo.getProblemid());
            words.add(String.valueOf(probleminfo.getProblemid()) + " " + title);
            map.put(title + " " + probleminfo.getProblemid(), probleminfo.getProblemid());
            map.put(String.valueOf(probleminfo.getProblemid()) + " " + title, probleminfo.getProblemid());
        }
        System.out.println(words);
        Trie trie = new Trie(words);//用trie树来加速查找
        List<String> results = trie.findWordsWithPrefix(info.getSpecial());

        for(String result1 : results){
            result.put(result1, map.get(result1));
        }
        return result;//返回查找到的结果
    }


    @PostMapping("/create")//这里还处理了前端传过来的数据，获取了contestid和各自的名字
    public void createContest(@RequestBody Contest info){
        info.getContenstcontent().setUsername(String.valueOf(userOperator.selectById(info.getContenstcontent().getUserid()).getUsername()));
        contestContentSQL.insert(info.getContenstcontent());
        List<ContestContent> contenstcontent = contestContentSQL.selectList(null);
        int id = contenstcontent.get(contenstcontent.size() - 1).getContestid();
        for(ContestAdmin admin: info.getContenstadmin()){
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

}
