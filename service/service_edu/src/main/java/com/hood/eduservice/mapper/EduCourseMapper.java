package com.hood.eduservice.mapper;

import com.hood.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hood.eduservice.entity.frontvo.CourseWebVo;
import com.hood.eduservice.entity.vo.CoursePublishVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-06-22
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    //根据课程id查询课程确认信息
    public CoursePublishVo getPublishCourseInfo(String courseId);

    //根据课程id，查询课程信息，涉及多表查询
    CourseWebVo getBaseCourseInfo(String courseId);
}
