package com.wyl.backend.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssConfig {
    @Bean
    // 使用代码嵌入的RAM用户的访问密钥配置访问凭证。
    public OSS ossClient() {
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(
                "LTAI5tD7ZrTGe9PerLNTDVr9",
                "hGJOH1ohpHkYHttzdHYuw9MSHOedcP"
        );
        return new OSSClientBuilder()
                .build(
                        "oss-cn-shenzhen.aliyuncs.com",
                        credentialsProvider
                );
    }
}
