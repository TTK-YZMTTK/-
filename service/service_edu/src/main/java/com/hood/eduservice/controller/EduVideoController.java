package com.hood.eduservice.controller;


import com.hood.commonutils.R;
import com.hood.eduservice.client.VodClient;
import com.hood.eduservice.entity.EduVideo;
import com.hood.eduservice.service.EduVideoService;
import com.hood.servicebase.exceptionhandler.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-06
 */
@RestController
@RequestMapping("/eduservice/video")
//@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    //注入VodClient
    @Autowired
    private VodClient vodClient;

    //添加小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        videoService.save(eduVideo);
        return R.ok();
    }

    //删除小节,在删除小节的时候，同时删除阿里与的视频
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id){
        //根据传进来的小节id获取视频id，再根据视频id进行删除.(存在小节没有视频的情况，使用需要判断一下)
        EduVideo eduVideo = videoService.getById(id);
        String videoSourceId = eduVideo.getVideoSourceId();
        //判断小节里是否有视频
        if(!StringUtils.isEmpty(videoSourceId)){
            //将得到的video id传到删除video的方法中（videoSourceId）
            R result  = vodClient.removeAlyVideo(videoSourceId);
            if(result.getCode() == 20001) {
                throw new MyException(20001,"删除视频失败，熔断器...");
            }
        }
        //删除小节。（先删视频，再删小节，顺序很重要，因为要根据小节id查视频id）
        videoService.removeById(id);
        return R.ok();
    }

    //修改小节  先查询后修改
    @GetMapping("getVideoInfo/{id}")
    public R getVideoInfo(@PathVariable String id){
        EduVideo eduVideo = videoService.getById(id);
        return R.ok().data("eduVideo",eduVideo);
    }

    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        videoService.updateById(eduVideo);
        return R.ok();
    }

}

