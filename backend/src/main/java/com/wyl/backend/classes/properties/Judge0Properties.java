package com.wyl.backend.classes.properties;

import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Judge0有关常量,从配置文件中读出
 */
@Component
@ToString
@ConfigurationProperties(prefix = "oj.judge0")
public class Judge0Properties {
    //    服务器地址
    public String SERVER_URL;
    //    提交单个题目的API
    public String PUSH_ONE_SUBMISSION_API;
    //    获取单个题目信息的API
    public String GET_API;
    //    提交多个个题目的API
    public String PUSH_MUL_SUBMISSION_API;
    //    获取多个题目信息的API
    public String GETS_MUL_RESULT_API;
    public void setSERVER_URL(String SERVER_URL) {
        this.SERVER_URL = SERVER_URL;
        PUSH_ONE_SUBMISSION_API = SERVER_URL + "submissions/?base64_encoded=true&wait=true";
        GET_API = SERVER_URL + "submissions/";
        PUSH_MUL_SUBMISSION_API = SERVER_URL + "submissions/batch?base64_encoded=true&wait=true";
        GETS_MUL_RESULT_API = SERVER_URL + "submissions/batch?base64_encoded=true&tokens=";
    }
}
