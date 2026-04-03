package com.xiaohongshu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xiaohongshu.mapper")
public class XiaohongshuApplication {
    public static void main(String[] args) {
        SpringApplication.run(XiaohongshuApplication.class, args);
    }
}