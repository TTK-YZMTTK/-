package com.hood.eduservice.service.impl;

import com.hood.eduservice.client.VodClient;
import com.hood.eduservice.entity.EduVideo;
import com.hood.eduservice.mapper.EduVideoMapper;
import com.hood.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-06
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    //注入vodclient
    @Autowired
    private VodClient vodClient;

    //根据课程id删小节
    @Override
    public void removeVideoByCourseId(String courseId) {
        //根据课程id查询所有视频id
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        wrapperVideo.select("video_source_id");
        List<EduVideo> eduVideoList = baseMapper.selectList(wrapperVideo);
        //将List<EduVideo>变成List<String>,因为要对立面的视频id进行拼接
        List<String> videoIds = new ArrayList<>();
        for (int i = 0; i < eduVideoList.size(); i++) {
            EduVideo eduVideo = eduVideoList.get(i);
            String videoSourceId = eduVideo.getVideoSourceId();
            if(!StringUtils.isEmpty(videoSourceId)){//防止该课程没有视频
                //放到videoIds集合中
                videoIds.add(videoSourceId);
            }
        }
        //根据多个视频id删除视频
        if(videoIds.size()>0){//防止集合为空
            vodClient.deleteBatch(videoIds);

        }
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
