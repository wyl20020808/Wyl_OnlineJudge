package com.wyl.backend.config;


import com.wyl.backend.classes.utils.SnowIdUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdGenerator {
    @Bean
    public SnowIdUtil userIdGenerator() {
        return new SnowIdUtil(0, 0);
    }

    @Bean
    public SnowIdUtil problemIdGenerator() {
        return new SnowIdUtil(0, 1);
    }

}
