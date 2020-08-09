package com.hood.eduservice.controller;


import com.hood.commonutils.R;
import com.hood.eduservice.entity.EduCourse;
import com.hood.eduservice.entity.vo.CourseInfoVo;
import com.hood.eduservice.entity.vo.CoursePublishVo;
import com.hood.eduservice.entity.vo.CourseQuery;
import com.hood.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-06
 */
@RestController
@RequestMapping("/eduservice/course")
//@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService eduCourseService;

    //查询所有课程
    @GetMapping("findAllCourse")
    public R getCourseList(){
        List<EduCourse> list = eduCourseService.list(null);
        return R.ok().data("list",list);
    }

    //分页查询课程
    @GetMapping("pageCourse")
    public R pageListCourse(@PathVariable long current,@PathVariable long limit){
        //创建page对象
        Page<EduCourse> pageCourse = new Page<>(current,limit);
        //调用page方法实现分页,将数据封装到page方法中
        eduCourseService.page(pageCourse,null);
        long total = pageCourse.getTotal();//总记录数
        List<EduCourse> records = pageCourse.getRecords();//数据list集合
        return R.ok().data("total",total).data("records",records);
    }

    //条件查询课程带分页
    @PostMapping("pageCourseCondition/{current}/{limit}")
    public R pageCourseCondition(@PathVariable long current,@PathVariable long limit,
                                 @RequestBody(required = false) CourseQuery courseQuery){
        //创建page对象
        Page<EduCourse> pageCourse = new Page<>(current,limit);
        //创建QueryWrapper对象
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //多条件组合查询，动态sql
        String title = courseQuery.getTitle();
        Integer status = courseQuery.getStatus();
        //判断条件是否为空，如果不为空则拼接条件
        if(!StringUtils.isEmpty(title)){
            //构建条件
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(status)){
            //构建条件
            wrapper.eq("status",status);
        }
        //调用方法实现条件查询带分页功能
        eduCourseService.page(pageCourse,wrapper);
        long total = pageCourse.getTotal();//总记录数
        List<EduCourse> records = pageCourse.getRecords();//数据list集合
        return R.ok().data("total",total).data("rows",records);
    }


    //添加课程基本信息的方法
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        //返回添加课程之后的课程ID，为了后面添加大纲使用
        String id = eduCourseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id);
    }

    //根据课程Id查询课程基本信息
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfoVo = eduCourseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }

    //修改课程信息
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        eduCourseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    //根据课程id查询课程确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id){
        CoursePublishVo coursePublishVo = eduCourseService.publishCourseInfo(id);
        return R.ok().data("publishCourse",coursePublishVo);
    }


    //课程最终发布，修改课程状态status。
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");
        eduCourseService.updateById(eduCourse);
        return R.ok();
    }

    //删除课程
    @DeleteMapping("{CourseId}")
    public R deleteCourse(@PathVariable String CourseId){
        eduCourseService.removeCourse(CourseId);
        return R.ok();
    }
}

