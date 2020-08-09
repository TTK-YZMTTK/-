package com.hood.oss.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


//当项目一启动，spring接口，spring加载之后，执行接口InitializingBean一个方法
@Component
public class ConstantPropertiesUtils implements InitializingBean {
    //读取配置文件
    @Value("${aliyun.oss.file.endpoint}")//给实体注入值
    private String endpoint;

    @Value("${aliyun.oss.file.keyid}")//给实体注入值
    private String keyId;

    @Value("${aliyun.oss.file.keysecret}")//给实体注入值
    private String keySecret;

    @Value("${aliyun.oss.file.bucketname}")//给实体注入值
    private String bucketname;

    //定义公开静态常量
    public static String END_POIND;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;
    @Override
    public void afterPropertiesSet() throws Exception {
        END_POIND = endpoint;
        ACCESS_KEY_ID = keyId;
        ACCESS_KEY_SECRET = keySecret;
        BUCKET_NAME =bucketname;
    }
}
