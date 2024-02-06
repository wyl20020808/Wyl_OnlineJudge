package com.wyl.backend.classes.LoginJWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String token = httpRequest.getHeader("Authorization");

        if (token == null || !token.startsWith("WYL ") || !token.equals("login")) {
            // 如果没有提供JWT或者格式不正确，直接返回401未授权错误
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization token is missing or invalid.");
            return; // 不再调用filterChain.doFilter，请求在此终止
        }

        try {
            String tokenTrimmed = token.substring(4);
            Algorithm algorithm = Algorithm.HMAC256("zhendehenxihuanwangxiaoya"); // 使用与生成JWT时相同的密钥和算法
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(tokenTrimmed);
            // 验证通过，可以在这里添加将用户信息设置到请求中的逻辑
        } catch (JWTVerificationException exception){
            // 如果验证失败，返回401未授权错误
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return; // 不再调用filterChain.doFilter，请求在此终止
        }

        filterChain.doFilter(request, response); // 验证成功，请求继续
    }
}