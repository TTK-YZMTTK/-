package com.hood.eduservice.service;

import com.hood.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-06
 */
public interface EduVideoService extends IService<EduVideo> {

    //根据课程id删小节
    void removeVideoByCourseId(String courseId);
}
