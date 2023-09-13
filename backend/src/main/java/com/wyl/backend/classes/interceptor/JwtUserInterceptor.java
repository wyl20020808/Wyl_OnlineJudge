package com.wyl.backend.classes.interceptor;

import com.wyl.backend.classes.utils.JwtUtil;
import com.wyl.backend.context.BaseContext;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;
    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtUtil.getTokenName());

        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = jwtUtil.parseJWT(token);
            Long empId = (Long) claims.get("userId");
            log.info("用户:{}校验成功", empId);
            //3、通过，记录用户id并放行
            BaseContext.setCurrentId(empId);
            return true;
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }
}
