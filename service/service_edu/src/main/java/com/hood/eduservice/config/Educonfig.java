package com.hood.eduservice.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@MapperScan("com.hood.eduservice.mapper")
public class Educonfig {

    /**
     * SQL执行性能分析插件
     * 开发环境使用，线上不推荐。maxTime值得是sql最大执行时间
     * @return
     */
    /*
    三种开发环境
    dev：开发环境
    test：测试环境
    prod（master）：生产环境
     */
    @Bean
    @Profile({"dev","test"})//设置dev test环境开启,需要在propertise中设置当前编码的开发环境
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        performanceInterceptor.setMaxTime(250);//ms，超过此处设置ms则sql不执行
        performanceInterceptor.setFormat(true);
        return performanceInterceptor;

    }
    //逻辑删除插件
    @Bean
    public ISqlInjector sqlInjector(){
        return new LogicSqlInjector();
    }

    //分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}
