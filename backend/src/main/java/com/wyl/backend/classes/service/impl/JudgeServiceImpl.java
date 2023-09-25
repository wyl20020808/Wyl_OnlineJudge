package com.wyl.backend.classes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyl.backend.classes.judge.*;
import com.wyl.backend.classes.judge.controller.Judge0Controller;
import com.wyl.backend.classes.judge.sql.JudgeContentSQL;
import com.wyl.backend.classes.problem.ProblemContent;
import com.wyl.backend.classes.problem.sql.ProblemContentSQL;
import com.wyl.backend.classes.problem.sql.SampleMapper;
import com.wyl.backend.classes.properties.Judge0Properties;
import com.wyl.backend.classes.service.JudgeService;
import com.wyl.backend.classes.utils.HttpClientUtil;
import com.wyl.backend.classes.utils.JsonUtil;
import com.wyl.backend.classes.utils.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
@Service
public class JudgeServiceImpl implements JudgeService {
    @Autowired
    Judge0Properties judge0Properties;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    SampleMapper sampleMapper;
    @Autowired
    ProblemContentSQL problemMapper;
    @Autowired
    OssUtil ossUtil;
    @Autowired
    Judge0Controller judge0Controller;
    @Autowired
    private JudgeContentSQL judgeContentSql;
    Set<String> free = new HashSet<>();
    Set<String> busy = new HashSet<>();
    public String PUSH_ONE_SUBMISSION_API = "submissions/?base64_encoded=false&wait=true";
    public String GET_API = "submissions/";

    public int last = 0;
    public void insertJudgecontentOne(JudgeContent judgeContent){
        judgeContentSql.insert(judgeContent);
    }
    @Override
    public Judge0Result judgeOne(Submission push, Map<String, String> MachineToToken, Map<String, String> TokenToMachine) {
                Map<String, Object> postBody = JsonUtil.convertObjectToMap(push);
        String result = null;
        String mathine = "false";
        List<Judge0> judge0 = judge0Controller.queryJugde0();//不断去查询状态
//        Random rand = new Random();
//        mathine = judge0.get(rand.nextInt(judge0.size())).getJudgemachine();
        mathine = judge0.get(last).getJudgemachine();
        last = last % 2;
//        while (true) {//直到有机子空闲为止
//            if (!mathine.equals("false")) break;
//            List<Judge0> judge0 = judge0Controller.queryJugde0();//不断去查询状态
//            free.clear();
//            busy.clear();
//            for (Judge0 judge : judge0) {//初始化一下
//                if (judge.getState().equals("false")) free.add(judge.getJudgemachine());
//                else busy.add(judge.getJudgemachine());
//                MachineToToken.put(judge.getJudgemachine(), judge.getToken());
//                TokenToMachine.put(judge.getToken(), judge.getJudgemachine());
//            }
//            //其实这里会涉及到同步问题，因为在几毫秒之内数据可能就被修改了，所以这里要加锁
//            for (String judge : free) {//遍历机器
//                if (!mathine.equals("false")) break;
//                //找机子的时候要拿它的token去判，去看看是不是已经判完了
//
//                mathine = judge;
//                judge0Controller.updateJugde0(mathine, "true", "无");
//            }
//
//            if (!mathine.equals("false")) break;
//            for (String judge : busy) {
//                //如果是true的机子就查一下它的token判完了没
//                //这里完成了所有机子的释放操作，在这里完成就够了，因为只要我要用我再去释放就行了
//                if (MachineToToken.get(judge).equals("无")) continue;
//
//                if (MachineToToken.get(judge).equals("无")) {
//
//                    mathine = judge;//我就用这台机器
//                    break;
//                };//测试
//                    //这个是为了同步状态，因为有可能机器被置为true，但是没有token
//
////                long startTime = System.currentTimeMillis();
//// 你的代码块
//                Judge0Result res = getOneJuge0ResultByToken(judge, MachineToToken.get(judge));
//                if (res.getStatus().getId() > 2) {//判题完成
////                    System.out.println("zxy");
////                    long endTime = System.currentTimeMillis();
////                    long timeElapsed = endTime - startTime;
////                    System.out.println("执行时间（毫秒）: " + timeElapsed);
//
//                    mathine = judge;//我就用这台机器
//                    judge0Controller.updateJugde0(mathine, "true", "无");
//
//                }
//            }
//        }


        String url = mathine + PUSH_ONE_SUBMISSION_API;
        try {
            result = String.valueOf(HttpClientUtil.doPostJson(url, postBody));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Judge0Result judge0Result = null;
        try {
            judge0Result = JsonUtil.jsonToObject(result, Judge0Result.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        System.out.println("判题结果：" +judge0Result);

        String token = judge0Result.getToken();
        try {
            // 创建一个FileWriter对象
            FileWriter writer = new FileWriter("output.txt", true); // true表示追加写入

            writer.write(mathine + " 这次用的机器 " + token +"\n"); // 写入文件

            // 关闭文件
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        MachineToToken.put(mathine, token);//记录机器对应的token
//        TokenToMachine.put(token, mathine);//记录token对应的机器
//        judge0Controller.updateJugde0(mathine, "true", token);
        return judge0Result;
    }

        @Override
    public Judge0Result getOneJuge0ResultByToken(String machine, String token){
        String url = machine + GET_API + token;
        String res = HttpClientUtil.doGet(url, null);
        Judge0Result result = null;
            long startTime = System.currentTimeMillis();

        System.out.println("正在查询结果："+url);
        try {
            result = JsonUtil.jsonToObject(res, Judge0Result.class);
            long endTime = System.currentTimeMillis();
            long timeElapsed = endTime - startTime;
            System.out.println("查询结果执行时间（毫秒）: " + timeElapsed);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

        /**
         * 同时判断多个实例，并返回token列表
         *
         * @param submissions
         * @return
         */
        public Judge0Result judgeOnlyOne (String stdin,long problemId, String source_code, int language_id){
            Map<String, String> MachineToToken = new ConcurrentHashMap<>();
            Map<String, String> TokenToMachine = new ConcurrentHashMap<>();
            int timelimit;
            int memorylimit;
            Judge0Result result = null;
            //        用来存储已经判题完成的题目结果
            //        获取题目限制（运行时间限制、地址空间限制）
//        ProblemContent problemContent = getLimitByProblemId(problemId);
//        timelimit = problemContent.getTimelimit();
//        memorylimit = problemContent.getMemorylimit();
            timelimit = 1;
            memorylimit = 128000;
           Submission submission = new Submission(source_code, language_id, stdin, "3", timelimit, memorylimit);
            System.out.println(source_code + " " + stdin + "opq") ;
           result = judgeOne(submission, MachineToToken, TokenToMachine);
            return result;
        }


    public JudgeResultToUser judgeProblem2 (String submittime,int userid,long problemId, String source_code, int language_id){
        Map<String, String> MachineToToken = new ConcurrentHashMap<>();
        Map<String, String> TokenToMachine = new ConcurrentHashMap<>();
        List<Judge0> judge0s = judge0Controller.queryJugde0();//这里得初始化，不然会出现同步错误
        int machineCount = judge0s.size();

        int timelimit;
        int memorylimit;
        Submissions submissions = null;
        LinkedList<Submission> arrayList = new LinkedList<Submission>();

        Judge0MulResult judgeResults = null;
        //        用来存储已经判题完成的题目结果
        List<Judge0Result> finishedSubmissions = new LinkedList<>();
        //        判题状态，>2表示判题结束
        int statusId;
        //        用户通过的示例的数量
        int rightCount = 0;
        //        用户未通过的示例的数量
        int errCount = 0;
        //        用来存放预期输出，key为题目的token，val为预期输出
        Map<String, String> expectOuts = new HashMap<>();

        //将源码转为base64
//        source_code = Base64Util.encode(source_code);

        //        获取题目限制（运行时间限制、地址空间限制）
//        ProblemContent problemContent = getLimitByProblemId(problemId);
//        timelimit = problemContent.getTimelimit();
//        memorylimit = problemContent.getMemorylimit();
        timelimit = 1;
        memorylimit = 128000;
        //        根据题目id查询出题目全部样例的输入输出
//        String path = "oj/" + problemId;
//        List<InAndOut> inOutFromOss = ossUtil.getInOutFromOss(path);
        String projectPath = System.getProperty("user.dir");
        String absolutePath = projectPath + File.separator + "dataSouce" + File.separator;
        String localPath = absolutePath + problemId ;
        List<InAndOut> inOutFromOss = ossUtil.getInOutFromLocal(localPath);
        ExecutorService executorService = Executors.newFixedThreadPool(machineCount * 2);//创建和服务器数量相同的线程
        int taskCount = inOutFromOss.size(); // 任务数量
        CountDownLatch latch = new CountDownLatch(taskCount);
        System.out.println(inOutFromOss + " " + "dxy" + " " + localPath);
        for (InAndOut s : inOutFromOss) {
            //从oss里面读取文件
            String finalSource_code = source_code;
            executorService.submit(() -> {
                //从oss里面读取文件
                try {
                    String stdIn = String.valueOf(ossUtil.readFileFromLocal(s.getIn()));
                    String stdOut = String.valueOf(ossUtil.readFileFromLocal(s.getOut()));

                    Submission submission = new Submission(finalSource_code, language_id, stdIn, stdOut, timelimit, memorylimit);
                    Judge0Result temp = judgeOne(submission, MachineToToken, TokenToMachine);
                    finishedSubmissions.add(temp);
                    JudgeContent judgeContent = new JudgeContent();
                    judgeContent.setJudgestate(temp.getStatus().getDescription());
                    judgeContent.setMemory(String.valueOf(temp.getMemory()));
                    judgeContent.setRuntime(temp.getTime());
                    judgeContent.setJudgeid(-1);
                    judgeContent.setUserid(userid);
                    judgeContent.setSubmittime(submittime);
                    judgeContent.setProblemid((int)problemId);
                    insertJudgecontentOne(judgeContent);//先插入进去
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown(); // 任务完成后调用countDown()
                }
            });
        }
        executorService.shutdown();

        try {
            latch.await(); // 等待所有任务完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //        统计答题情况并合并预期输出
        for (Judge0Result res : finishedSubmissions) {
            statusId = res.getStatus().getId();
            if (statusId == 3) {
                rightCount++;
            } else {
                errCount++;
            }
            //  设置预期输出
            res.setExpectOut(expectOuts.get(res.getToken()));
        }

        //        记录答题情况
        return new JudgeResultToUser(rightCount + errCount, rightCount, errCount, errCount == 0, finishedSubmissions);
    }
    public JudgeResultToUser judgeProblem (long problemId, String source_code, int language_id){
        Map<String, String> MachineToToken = new ConcurrentHashMap<>();
        Map<String, String> TokenToMachine = new ConcurrentHashMap<>();

        List<Judge0> judge0s = judge0Controller.queryJugde0();//这里得初始化，不然会出现同步错误

        int machineCount = judge0s.size();
        //就是那个判断机子是否判题完成那里
//        for(Judge0 judge0 : judge0s){
//            if(judge0.getState().equals("false"))free.add(judge0.getJudgemachine());
//            else busy.add(judge0.getJudgemachine());
//            MachineToToken.put(judge0.getJudgemachine(),judge0.getToken());
//            TokenToMachine.put(judge0.getToken(),judge0.getJudgemachine());
//        }
        //        题目的时间限制
        int timelimit;
        //        题目的空间限制
        int memorylimit;
        //        输入暂存

        //        示例暂存
        Submissions submissions = null;
        //        打包后的全部的示例
        LinkedList<Submission> arrayList = new LinkedList<Submission>();
        //        用来存储每次调用/submissions/batch接口获取的判题查询结果
        Judge0MulResult judgeResults = null;
        //        用来存储已经判题完成的题目结果
        List<Judge0Result> finishedSubmissions = new LinkedList<>();
        //        判题状态，>2表示判题结束
        int statusId;
        //        用户通过的示例的数量
        int rightCount = 0;
        //        用户未通过的示例的数量
        int errCount = 0;
        //        用来存放预期输出，key为题目的token，val为预期输出
        Map<String, String> expectOuts = new HashMap<>();

        //将源码转为base64
//        source_code = Base64Util.encode(source_code);

        //        获取题目限制（运行时间限制、地址空间限制）
//        ProblemContent problemContent = getLimitByProblemId(problemId);
//        timelimit = problemContent.getTimelimit();
//        memorylimit = problemContent.getMemorylimit();
        timelimit = 1;
        memorylimit = 128000;
        //        根据题目id查询出题目全部样例的输入输出
//        String path = "oj/" + problemId;
//        List<InAndOut> inOutFromOss = ossUtil.getInOutFromOss(path);
        String projectPath = System.getProperty("user.dir");
        String absolutePath = projectPath + File.separator + "dataSouce" + File.separator;
        String localPath = absolutePath + problemId ;
        List<InAndOut> inOutFromOss = ossUtil.getInOutFromLocal(localPath);
//        String path = ""
//10
//        List<String> judge0Tokens = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(machineCount * 2);//创建和服务器数量相同的线程
//        ExecutorCompletionService<String> completionService = new ExecutorCompletionService<>(executorService);
        //有序地获取结果 //没有仔细分析会不会影响效率，感觉应该是不会的。他是采用阻塞的方式去用队列维护顺序的
        //        将全部的样例打包成Submissions
//        ConcurrentLinkedQueue<String> judge0Tokens2 = new ConcurrentLinkedQueue<>();
        int taskCount = inOutFromOss.size(); // 任务数量
        CountDownLatch latch = new CountDownLatch(taskCount);
        System.out.println(inOutFromOss + " " + "dxy" + " " + localPath);
        for (InAndOut s : inOutFromOss) {
            //从oss里面读取文件
            String finalSource_code = source_code;
            executorService.submit(() -> {
                //从oss里面读取文件
                try {
                    String stdIn = String.valueOf(ossUtil.readFileFromLocal(s.getIn()));
                    String stdOut = String.valueOf(ossUtil.readFileFromLocal(s.getOut()));
                    System.out.println(stdIn + " " + stdOut + " xyz");
//                    stdIn = Base64Util.encode(stdIn);
//                    stdOut = Base64Util.encode(stdOut);
                    Submission submission = new Submission(finalSource_code, language_id, stdIn, stdOut, timelimit, memorylimit);
                    finishedSubmissions.add(judgeOne(submission, MachineToToken, TokenToMachine));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown(); // 任务完成后调用countDown()
                }
            });
        }

        executorService.shutdown();

        try {
            latch.await(); // 等待所有任务完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//
//        for(String token :judge0Tokens2){
//            judge0Tokens.add(token);//转移过来
//            System.out.println(token + " 已加入队列");
//        }
//        System.out.println(judge0Tokens);
//        try {
//            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
//
//                executorService.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            for(String token :judge0Tokens2){
//                judge0Tokens.add(token);//转移过来
//            }
//            executorService.shutdownNow();
//        }

//        System.out.println(arr);
//        System.out.println("shdj");
//        submissions.setSubmissions(arr);
//
        //        发送Submissions至judge0并获取List<Token>
//        List<Judge0Token> judge0Tokens = judgeMul(submissions);

        //        list是有顺序的，tokens是按照submission的顺序创建的，使用循环合并这两者,以便根据token获取测试点的预期输出
//        List<Submission> submissionList = submissions.getSubmissions();
//        for (int i = 0, j = 0; i < judge0Tokens.size() && j<submissionList.size() ;) {
//            expectOuts.put(judge0Tokens.get(i++),submissionList.get(j++).getExpected_output());
//        }


        //        查询判题结果
//        List<String> temp = new ArrayList<>();
//        while (!judge0Tokens.isEmpty()) {
//            //        根据List<Token>查询判题结果
//            System.out.println("停下来了吗");
//            for (String token : judge0Tokens) {
//                System.out.println("正在查找判题进度:" + token);
//                Judge0Result res = getOneJuge0ResultByToken(TokenToMachine.get(token), token);
//                statusId = res.getStatus().getId();
//                if (statusId > 2) {//            取出已经出来的判题结果
//                    finishedSubmissions.add(res);
//                    temp.add(token);
//                    System.out.println("已经判题完毕" + token);
//                }
//            }
//            for (String token : temp) {
//                System.out.println("删除已经判题完毕的token" + token);
//                judge0Tokens.removeIf(tokens -> Objects.equals(token, tokens));
//            }
//            temp.clear();
//            judgeResults = getMulJuge0ResultByToken(judge0Tokens);
//
//            //        从List<Token>中删除判题结果已经出来的token
//            for (Judge0Result res : judgeResults.getSubmissions()) {
//                statusId = res.getStatus().getId();
//                if (statusId > 2) {//            取出已经出来的判题结果
//                    finishedSubmissions.add(res);
////                    String machine = TokenToMachine.get(res.getToken());
////                    judgeController.updateJugde0(machine,"false","" + "无");//已经评测完了，释放机子
//                    judge0Tokens.removeIf(token -> Objects.equals(res.getToken(), token.getToken()));
//                }
//
//            }
//            sleep(100);
//        }

        //        统计答题情况并合并预期输出
        for (Judge0Result res : finishedSubmissions) {
            statusId = res.getStatus().getId();
            if (statusId == 3) {
                rightCount++;
            } else {
                errCount++;
            }
            //  设置预期输出
            res.setExpectOut(expectOuts.get(res.getToken()));
        }

        //        记录答题情况
        return new JudgeResultToUser(rightCount + errCount, rightCount, errCount, errCount == 0, finishedSubmissions);
    }

        /**
         * 以下是工具方法
         *
         */


        /**
         * 根据ProblemId 来获取问题的限制
         *
         * @param problemId
         * @return
         */
        private ProblemContent getLimitByProblemId (long problemId){
            QueryWrapper<ProblemContent> lqw = new QueryWrapper<ProblemContent>();
            lqw.select("timelimit", "memorylimit");
            lqw.apply("problemid=" + problemId);
            return problemMapper.selectOne(lqw);
        }

        private void sleep ( int time){
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
