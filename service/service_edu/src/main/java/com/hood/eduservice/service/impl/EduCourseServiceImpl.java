package com.hood.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hood.eduservice.entity.EduCourse;
import com.hood.eduservice.entity.EduCourseDescription;
import com.hood.eduservice.entity.frontvo.CourseFrontVo;
import com.hood.eduservice.entity.frontvo.CourseWebVo;
import com.hood.eduservice.entity.vo.CourseInfoVo;
import com.hood.eduservice.entity.vo.CoursePublishVo;
import com.hood.eduservice.mapper.EduCourseMapper;
import com.hood.eduservice.service.EduChapterService;
import com.hood.eduservice.service.EduCourseDescriptionService;
import com.hood.eduservice.service.EduCourseService;
import com.hood.eduservice.service.EduVideoService;
import com.hood.servicebase.exceptionhandler.MyException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-06
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    //注入课程描述这个类，联动
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    //注入小节和章节的service，因为要在这里，删除课程的时候删除这些相关的东西，要对他们进行操作
    @Autowired
    private EduChapterService ChapterService;
    @Autowired
    private EduVideoService eduVideoService;

    //添加课程基本信息的方法
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1 向课程表添加课程基本信息
        //CourseInFoVo对象转换EduCourse对象,然后将转换好的对象写入实体（就是写到数据库里面去，eduCourse代表数据库实体）
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = baseMapper.insert(eduCourse);
        //insert返回一个类似于行数的值，你懂的。
        if (insert == 0){
            //添加失败
            throw new MyException(20001,"添加课程失败");
        }
        //获取添加课程信息之后的ID，因为下面课程简介自动生成的ID与上面不一样（不在一张表）
        String cid = eduCourse.getId();
        //2 向课程简介表添加信息,因为这是implements EduCourseService的类，所以无法使用baseMapper来添加，baseMapper添加的话会
        //加到EduCourse里面去，我们是要加到EduCourseDescription里，所以要和EduCourseDescriptionServiceImpl联动，故Auto注入（滑稽.jpg）
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfoVo.getDescription());
        courseDescription.setId(cid);
        courseDescriptionService.save(courseDescription);
        return cid;
    }

    //根据课程Id查询课程基本信息
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        //查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();


        BeanUtils.copyProperties(eduCourse,courseInfoVo);

        //查询描述表
        EduCourseDescription courseDescription = courseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(courseDescription.getDescription());


        return courseInfoVo;
    }

    //修改课程信息
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int update = baseMapper.updateById(eduCourse);
        if (update == 0){
            //添加失败
            throw new MyException(20001,"修改课程信息失败");
        }
        //修改课程描述表
        EduCourseDescription description = new EduCourseDescription();
        description.setId(courseInfoVo.getId());
        description.setDescription(courseInfoVo.getDescription());
        courseDescriptionService.updateById(description);
    }

    //根据课程id查询课程确认信息
    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        //调用mapper
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(id);
        return publishCourseInfo;
    }

    //删除课程
    @Override
    public void removeCourse(String courseId) {
        //1小节2章节3描述4本身
        eduVideoService.removeVideoByCourseId(courseId);
        ChapterService.removeByCourseId(courseId);
        courseDescriptionService.removeById(courseId);//由于是一一对应的关系，所以直接在这里删除
        int result = baseMapper.deleteById(courseId);
        if(result == 0){
            throw new MyException(20001,"删除失败");
        }
    }

    //条件查询带分页查询课程
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> pageParam, CourseFrontVo courseFrontVo) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //判断条件值是否为空，不为空则需要拼接sql语句
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())){//判断一级分类
            wrapper.eq("subject_parent_id",courseFrontVo.getSubjectParentId());
        }
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectId())){//判断二级分类
            wrapper.eq("subject_id",courseFrontVo.getSubjectId());
        }
        if(!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())){//是否按关注度排序
            wrapper.orderByDesc("buy_count");
        }
        if(!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())){//是否按最新发布排序
            wrapper.orderByDesc("gmt_create");
        }
        if(!StringUtils.isEmpty(courseFrontVo.getPriceSort())){//是否按价格排序
            wrapper.orderByDesc("price");
        }
        baseMapper.selectPage(pageParam,wrapper);
        List<EduCourse> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();//是否有下一页
        boolean hasPrevious = pageParam.hasPrevious();//是否有上一页

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;

    }

    //根据课程id，编写sql语句查询，要去mapper中
    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        return baseMapper.getBaseCourseInfo(courseId);
    }
}
