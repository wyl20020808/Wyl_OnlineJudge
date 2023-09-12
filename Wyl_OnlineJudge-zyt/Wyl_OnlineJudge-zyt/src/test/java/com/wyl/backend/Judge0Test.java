package com.wyl.backend;

import com.wyl.backend.properties.Judge0Properties;
import com.wyl.backend.entity.judge.*;
import com.wyl.backend.service.JudgeService;
import com.wyl.backend.utils.Base64Util;
import com.wyl.backend.utils.FileUtil;
import com.wyl.backend.utils.HttpClientUtil;
import com.wyl.backend.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
public class Judge0Test {
    @Autowired
    Judge0Properties judge0Properties;
    @Autowired
    JudgeService judgeService;

    /**
     * 提交题目
     */
    @Test
    void judgePush() throws IOException {
        String source_code = "cHVibGljIGNsYXNzIE1haW4gewogICAgcHVibGljIHN0YXRpYyB2b2lkIG1haW4oU3RyaW5nW10gYXJncykgewogICAgICAgIFN5c3RlbS5vdXQucHJpbnRsbigiaGVsbG8sIHdvcmxkIik7CiAgICB9Cn0K";
        Submission submission = Submission.builder()
                .source_code(source_code)
                .language_id(62)
                .build();

        log.info("entity=>{}", submission.toString());
        Map<String, Object> body = JsonUtil.convertObjectToMap(submission);
        String url = judge0Properties.PUSH_ONE_SUBMISSION_API;
        StringBuilder result = HttpClientUtil.doPostJson(url, body);
        log.info("res=>{}", result);
    }

    /**
     * 获取已经提交的题目信息
     *
     * @throws Exception
     */
    @Test
    void judgeGet() throws Exception {
        log.info("getAPI=>{}", judge0Properties.GET_API);
        String url = judge0Properties.GET_API + "216f882c-1338-4994-926f-a4b927920900";
        String res = HttpClientUtil.doGet(url, null);
        System.out.println(res);
        Judge0Result endRes = JsonUtil.jsonToObject(res, Judge0Result.class);
        System.out.println(endRes.toString());
    }

    /**
     * 测试：向judge0发送单个需要读取较大文件的实例
     *
     * @throws IOException
     */
    @Test
    void judgePushWithFile() throws IOException {
        String pathSource = "D:\\CodeLife\\Code\\webStudy\\code\\Wyl_OnlineJudge\\judgeTestFile\\";
        //读取文件内容并转为Base64编码
        String stdin = FileUtil.readFileToBase64(pathSource + "kaspfa.in");
        String source_code = FileUtil.readFileToBase64(pathSource + "code.cpp");
        String expected_output = FileUtil.readFileToBase64(pathSource + "kaspfa.out");
        String url = judge0Properties.PUSH_ONE_SUBMISSION_API;

        Submission submission = new Submission();
        submission.setSource_code(source_code);
        submission.setStdin(stdin);
        submission.setExpected_output(expected_output);
        submission.setLanguage_id(54);
        submission.setCpu_time_limit(5);
        submission.setCpu_extra_time(5);

        Map<String, Object> body = JsonUtil.convertObjectToMap(submission);
        log.info("url:{}", url);
        StringBuilder result = HttpClientUtil.doPostJson(url, body);

        FileUtil.insertTextToFile(pathSource + "res.txt", String.valueOf(result));
    }


    @Test
    void testBase64Util() {
        String source = "source";
        log.info("sourc=>{}", source);
        log.info("base64=>{}", Base64Util.encode(source));
    }

    /**
     * 测试：向Judge0发送多个实例
     */
    @Test
    void judgePushMul() throws Exception {
        String json = "{\n" +
                "  \"submissions\": [\n" +
                "    {\n" +
                "      \"language_id\": 46,\n" +
                "      \"source_code\": \"echo hello from Bash\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"language_id\": 71,\n" +
                "      \"source_code\": \"print(\\\"hello from Python\\\")\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"language_id\": 72,\n" +
                "      \"source_code\": \"puts(\\\"hello from Ruby\\\")\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        Submissions submissions = JsonUtil.jsonToObject(json, Submissions.class);

        List<Judge0Token> judge0Tokens = judgeService.judgeMul(submissions);
        System.out.println(judge0Tokens.toString());
    }

    @Test
    void getProperties(){
        System.out.println(judge0Properties.toString());
    }

    @Test
    void getMulResult(){
        List<Judge0Token> tokens = new LinkedList<>();
        tokens.add(new Judge0Token("7974fec5-6431-4841-b1cc-5825cd221831"));
        tokens.add(new Judge0Token("0b1c229e-5c87-4228-9352-2e9c6f6cfa8f"));
        tokens.add(new Judge0Token("90f43253-c642-4a31-b840-09ba95ae0711"));
        Judge0MulResult res = judgeService.getMulJuge0ResultByToken(tokens);
        System.out.println(res);
    }

    /**
     * 测试正式的判题Service
     */
    @Test
    void testJudgeResultToUser(){
        String source_code = "import java.util.Scanner;  \n" +
                "public class Main {  \n" +
                "    public static void main(String[] args) {  \n" +
                "        Scanner scanner = new Scanner(System.in);  \n" +
                "        int a = scanner.nextInt();  \n" +
                "        int b = scanner.nextInt();  \n" +
                "        int sum = a + b;  \n" +
                "        System.out.println(sum);  \n" +
                "    }  \n" +
                "}";
        JudgeResultToUser judgeResultToUser = judgeService.judgeProblem(1, source_code, 62);
        System.out.println(judgeResultToUser.toString());
    }
}