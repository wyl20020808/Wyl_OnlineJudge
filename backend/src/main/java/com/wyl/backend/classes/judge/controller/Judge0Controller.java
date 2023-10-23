package com.wyl.backend.classes.judge.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wyl.backend.classes.judge.Judge0;
import com.wyl.backend.classes.judge.sql.Judge0SQL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/")
public class Judge0Controller {
    @Autowired
    private Judge0SQL judge0SQL;
    /**
     * 编译程序，并返回编译结果
     * 返回用户自己提交的实例测试结果
     * @param push 实例信息
     * @return  判题结果
     * @throws Exception
     */


    public List<Judge0> queryJugde0(){ //获取所有的机器状态
        return judge0SQL.selectList(null);
    }
    //    public List<Judge0> queryJugde0TokenByMachine(){ //通过机器
//        return judge0SQL.selectList(null);
//    }
    public void updateJugde0(String judgemachine, String state,String token){//更新机器状态
        UpdateWrapper<Judge0> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("judgemachine",judgemachine);
        if(state.equals("true") || state.equals("false")) updateWrapper.set("state",state);

        updateWrapper.set("token",token);
        judge0SQL.update(null,updateWrapper);
    }
}
