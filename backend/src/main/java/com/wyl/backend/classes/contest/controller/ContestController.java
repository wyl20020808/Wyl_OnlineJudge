package com.wyl.backend.classes.contest.controller;

import com.wyl.backend.classes.contest.ContestContent;
import com.wyl.backend.classes.contest.Contest;
import com.wyl.backend.classes.contest.ContestAdmin;
import com.wyl.backend.classes.contest.ContestProblem;
import com.wyl.backend.classes.contest.SQL.ContestAdminSQL;
import com.wyl.backend.classes.contest.SQL.ContestContentSQL;
import com.wyl.backend.classes.contest.SQL.ContestProblemSQL;
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
    private ContestContentSQL contestCreateSQL;

    @Autowired
    private ContestAdminSQL  contestAdminSQL;
    @Autowired
    private ContestProblemSQL   contestProblemSQL;
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
        contestCreateSQL.insert(info.getContenstcontent());
        List<ContestContent> contenstcontent = contestCreateSQL.selectList(null);
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
        return contestCreateSQL.selectList(null);
    }
}
