package com.hood.eduservice.service;

import com.hood.eduservice.entity.EduChapter;
import com.hood.eduservice.entity.chapter.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-06
 */
public interface EduChapterService extends IService<EduChapter> {

    //返回课程大纲列表,根据课程id进行查询
    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    //删除章节的方法
    boolean deleteChapter(String chapterId);

    //根据课程id删章节
    void removeByCourseId(String courseId);
}
