package com.hood.vod.controller;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.hood.commonutils.R;
import com.hood.servicebase.exceptionhandler.MyException;
import com.hood.vod.Utils.ConstantVodUtils;
import com.hood.vod.Utils.InitVodClient;
import com.hood.vod.service.VodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
//@CrossOrigin
public class VodController {

    @Autowired
    private VodService vodService;


    //上传视频到阿里云的方法
    @PostMapping("uploadAlyiVideo")
    public R  uploadAlyiVideo(MultipartFile file){
        //返回视频上传之后生成的id值
       String videoId = vodService.uploadVideoAly(file);
        return R.ok().data("videoId",videoId);
    }

    //根据视频id删除阿里云的视频
    @DeleteMapping("removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable String id){
        try{
            //初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建删除视频的request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //向request里面设置视频id
            request.setVideoIds(id);
            //调用初始化里面的方法实现删除
            client.getAcsResponse(request);
            return R.ok();
        }catch(Exception e){
            throw new MyException(20001,"删除失败");
        }
    }

    //删除多个阿里云视频的方法
    //删除多个视频id List videoIdList
    @DeleteMapping("delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList){
        vodService.removeMoreAlyVideo(videoIdList);
        return R.ok();
    }

    //根据视频id获取视频的播放凭证
    @GetMapping("getPlayAuth/{id}")
    public R getPlayAuth(@PathVariable String id){
        try{
            //创建初始化对象
            DefaultAcsClient client =
                    InitVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建获取凭证的request和resource对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            //向request中设置视频id
            request.setVideoId(id);
            //调用方法，得到凭证
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return R.ok().data("playAuth",playAuth);
        }catch (Exception e){
            throw new MyException(20001,"获取凭证失败");
        }
    }

}


