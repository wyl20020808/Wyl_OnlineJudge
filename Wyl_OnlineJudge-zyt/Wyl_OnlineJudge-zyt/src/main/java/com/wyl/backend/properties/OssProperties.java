package com.wyl.backend.properties;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 因为始终没办法在Bean中获取到yml里面的参数，所以这个类暂时没有用
 */

@Component
@Data
@ConfigurationProperties(prefix = "oss")
public class OssProperties {
    // RAM用户的访问密钥（AccessKey ID和AccessKey Secret）。
    public static String accessKeyId;
    public static String accessKeySecret;
    // Endpoint，服务器的地址
    public static String endpoint;
    // 填写Bucket名称，例如examplebucket。
    public static String bucketName;
    // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
    public static String pathIn;
    public static String pathOut;
}
