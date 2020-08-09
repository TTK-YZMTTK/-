package com.hood.eduservice.client;


import com.hood.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * nacos调用方法使用的类
 */

@FeignClient(name = "service-vod",fallback = VodFileDegradeFeginClient.class)//指定要调用的模块，fallback，熔断器指定类，出错就用指定的类
@Component
public interface VodClient {

    //定义要调用方法的路径,路径要写全
    //根据视频id删除阿里云的视频
    @DeleteMapping("/eduvod/video/removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable("id") String id);
    //此处的pathvariable必须制定参数名称，否则报错


    //删除多个阿里云视频的方法
    //删除多个视频id List videoIdList
    @DeleteMapping("/eduvod/video/delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);
}
