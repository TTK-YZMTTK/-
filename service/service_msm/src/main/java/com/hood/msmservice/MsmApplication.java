package com.hood.msmservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;



@ComponentScan({"com.hood"}) //指定扫描位置
@MapperScan("com.hood.msmservice.mapper")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消自动加载数据库
public class MsmApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsmApplication.class, args);
    }
}