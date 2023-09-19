//package com.wyl.backend.config;
//
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//
//@Slf4j
//@Configuration
//public class WebMvcConfiguration extends WebMvcConfigurationSupport {
////    @Autowired
////    private JwtUserInterceptor jwtUserInterceptor;
////    /**
////     * 注册登录拦截器
////     *
////     * @param registry
////     */
//////    protected void addInterceptors(InterceptorRegistry registry) {
//////        log.info("开始注册登录拦截器...");
//////        String[] excludePathPatterns = new String[]{
//////                "/user/signin",
//////                "/user/login"
//////        };
//////        registry.addInterceptor(jwtUserInterceptor)
//////                .addPathPatterns("/**")
//////                .excludePathPatterns(excludePathPatterns);
//////    }
//}
