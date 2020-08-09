package com.hood.eduservice.service.impl;

import com.hood.eduservice.entity.EduChapter;
import com.hood.eduservice.entity.EduVideo;
import com.hood.eduservice.entity.chapter.ChapterVo;
import com.hood.eduservice.entity.chapter.VideoVo;
import com.hood.eduservice.mapper.EduChapterMapper;
import com.hood.eduservice.service.EduChapterService;
import com.hood.eduservice.service.EduVideoService;
import com.hood.servicebase.exceptionhandler.MyException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-06
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    //注入小节的service，和课程注释那里注入description一样
    @Autowired
    private EduVideoService eduVideoService;

    //返回课程大纲列表,根据课程id进行查询
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        //根据课程id查出里面所有的章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);

        //根据课程id查出里面所有的小节
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        List<EduVideo> eduVideoList = eduVideoService.list(wrapperVideo);

        List<ChapterVo> finalList = new ArrayList<>();
        //遍历查出的章节list集合进行封装
        for (int i = 0; i < eduChapterList.size(); i++) {
            EduChapter eduChapter = eduChapterList.get(i);
            ChapterVo chapterVo = new ChapterVo();//因为下面方法要用到ChapterVo的对象，所以这里需要创建
            BeanUtils.copyProperties(eduChapter,chapterVo);
            finalList.add(chapterVo);

            List<VideoVo> videoList = new ArrayList<>();
            //遍历查询小节list集合，进行封装
            for (int j = 0; j < eduVideoList.size(); j++) {
                EduVideo eduVideo = eduVideoList.get(j);
                //判断：小节里面的chapterid和章节里面的id是否一样
                if(eduVideo.getChapterId().equals(eduChapter.getId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoList.add(videoVo);
                }
            }
            //章节包含小节
            chapterVo.setChildren(videoList);
        }
        //这里是啥则前台返回啥。
        return finalList;
    }

    //删除章节的方法
    @Override
    public boolean deleteChapter(String chapterId) {
        //先根据章节ID查询是否存在小节，有的话留一手，没有的话删除
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        int count = eduVideoService.count(wrapper);//返回符合条件的值有几个
        if (count > 0 ) {
            throw new MyException(20001,"存在小节，无法删除");
        }else {
            int result = baseMapper.deleteById(chapterId);
            return result>0;
        }

    }

    //根据课程id删章节
    @Override
    public void removeByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
