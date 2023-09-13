package com.wyl.backend.classes.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Base64工具类
 * 为了避免用户提交的内容包含不可打印的字符，使用base64编码确保稳定
 */
public class Base64Util {
    //加密
    public static String encode(String input) {
        byte[] encodedBytes = Base64.getEncoder().encode(input.getBytes(StandardCharsets.UTF_8));
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }
    //解密
    public static String decode(String encodedInput) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedInput.getBytes(StandardCharsets.UTF_8));
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
