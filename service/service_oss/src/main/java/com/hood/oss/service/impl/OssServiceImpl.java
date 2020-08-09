package com.hood.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.hood.oss.service.OssService;
import com.hood.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;


@Service
public class OssServiceImpl implements OssService {

    //上传文件到oss
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        // 工具类获取值
        String endpoint = ConstantPropertiesUtils.END_POIND;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        try {
            // 创建OSS实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            //获取上传文件输入流
            InputStream inputStream = file.getInputStream();
            //获取文件源名称
            String fileName = file.getOriginalFilename();
            //为了避免名称覆盖问题，引入UUID,并将横杠去除
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            fileName = uuid+fileName;
            //让上传的文件自动按日期分类
            String datePath = new DateTime().toString("yyyy/MM/dd");
            //拼接
            fileName=datePath+"/"+fileName;
            //调用oss方法实现上传
            //第一个参数bucke名称，第二个参数：上传到oss文件的名称和路径 /aa/bb/1.jpg
            //第三个 文件输入流inputStream
            ossClient.putObject(bucketName,fileName,inputStream);
            //关闭ossClient
            ossClient.shutdown();
            //把上传之后文件路径返回，有个问题，阿里云oss不能在一个文件内存储相同名称的文件
            //需要把上传到阿里云oss路径手动拼接
            String url = "https://"+bucketName+"."+endpoint+"/"+fileName;
            return url;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        }
    }