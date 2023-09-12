package com.wyl.backend;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wyl.backend.entity.judge.Submission;
import com.wyl.backend.entity.problem.ProblemContent;
import com.wyl.backend.entity.problem.ProblemSample;
import com.wyl.backend.mapper.ProblemMapper;
import com.wyl.backend.mapper.SampleMapper;
import com.wyl.backend.mapper.UserMapper;
import com.wyl.backend.service.JudgeService;
import com.wyl.backend.service.SampleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SqlTest {

    @Autowired
    UserMapper userMapper;
    @Autowired
    SampleMapper sampleMapper;
    @Autowired
    ProblemMapper problemMapper;
    @Autowired
    SampleService sampleService;
    @Autowired
    JudgeService judgeService;

    /**
     * 测试mybatis是否能用
     */
    @Test
    void testMybatis() {
        System.out.println(userMapper.selectList(null).toString());
    }

    /**
     * 测试mybatis-plus是否能用
     */
    @Test
    void testMP(){
        System.out.println(userMapper.selectList(null).toString());
    }

//    @Test
//    void testGetSamples(){
//        QueryWrapper<ProblemSample> lqw = new QueryWrapper<ProblemSample>();
//        lqw.select("input","output");
//        lqw.apply("problemid="+1);
//        List<ProblemSample> samples = sampleMapper.selectList(lqw);
//        System.out.println(samples.toString());
//    }


    @Test
    void testGetProblemContent(){
        QueryWrapper<ProblemContent> lqw = new QueryWrapper<ProblemContent>();
        lqw.select("timelimit","memorylimit");
        lqw.apply("problemid="+1);
        ProblemContent problemContent = problemMapper.selectOne(lqw);
        System.out.println(problemContent.toString());
    }

    @Test
    void testOriginSubmission(){
        Submission submission = new Submission("code", 1, "in", "out", 2, 128000);
        System.out.println(submission.toString());
    }

//    @Test
//    void testDeletSampleId(){
//        QueryWrapper<ProblemSample> lqw = new QueryWrapper<>();
//        lqw.apply("problemid="+1);
//        sampleMapper.delete(lqw);
//    }

//    @Test
//    void getPath(){
//        QueryWrapper<ProblemSample> lqw = new QueryWrapper<>();
//        lqw.apply("problemid=" + 1);
//        ProblemSample problemSample = sampleMapper.selectOne(lqw);
//        String path = problemSample.getPath();
//        System.out.println(path);
//    }

    @Test
    void getSampleById(){

        QueryWrapper<ProblemContent> lqw = new QueryWrapper<>();
        lqw.apply("problemid="+2);
        ProblemContent problemContent = problemMapper.selectOne(lqw);
        System.out.println(problemContent);
    }

}
