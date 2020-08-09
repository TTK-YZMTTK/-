package com.hood.eduorder;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;



@ComponentScan({"com.hood"}) //指定扫描位置
@MapperScan("com.hood.eduorder.mapper")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消自动加载数据库
@EnableDiscoveryClient//远程注册
@EnableFeignClients//远程调用
public class OrdersApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrdersApplication.class, args);
    }
}