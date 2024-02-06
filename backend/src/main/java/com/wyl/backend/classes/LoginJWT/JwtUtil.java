package com.wyl.backend.classes.LoginJWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;

public class JwtUtil {

    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000; // 设置过期时间，例如24小时
    private static final String SECRET = "zhendehenxihuanwangxiaoya"; // 密钥，应该保密

    public static String generateToken(String userId) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        // 生成JWT
        return JWT.create()
                .withClaim("userId", userId) // 将用户ID作为一个声明
                .withExpiresAt(date) // 设置过期时间
                .sign(algorithm); // 签名
    }
}