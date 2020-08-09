package com.hood.eduservice.controller.front;

import com.hood.commonutils.JwtUtils;
import com.hood.commonutils.R;
import com.hood.commonutils.ordervo.CourseWebVoOrder;
import com.hood.eduservice.client.OrdersClient;
import com.hood.eduservice.entity.EduCourse;
import com.hood.eduservice.entity.EduTeacher;
import com.hood.eduservice.entity.chapter.ChapterVo;
import com.hood.eduservice.entity.frontvo.CourseFrontVo;
import com.hood.eduservice.entity.frontvo.CourseWebVo;
import com.hood.eduservice.service.EduChapterService;
import com.hood.eduservice.service.EduCourseService;
import com.hood.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/coursefront")
//@CrossOrigin
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrdersClient ordersClient;


    //1 条件查询带分页查询课程
    @PostMapping("getFrontCourseList/{page}/{limit}")
    public R getFrontCourseList(@PathVariable long page,@PathVariable long limit,
                                @RequestBody(required = false) CourseFrontVo courseFrontVo){
        //(required = false)加上这个里面的值可以为空，不然报错
        Page<EduCourse> pageCourse = new Page<>(page,limit);
        Map<String,Object> map = courseService.getCourseFrontList(pageCourse,courseFrontVo);
        return R.ok().data(map);
    }




    //2 课程详情的方法
    @GetMapping("getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request){
        //根据课程id，编写sql语句查询课程信息,因为和讲师不同，涉及多表查询，需要创建新的vo对象
        CourseWebVo courseWebVo = courseService.getBaseCourseInfo(courseId);
        //根据课程id，查询章节和小节部分
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoByCourseId(courseId);
        //根据课程id和用户id查询当前课程是否支付过了,用户id可以在token中取
        String memberIdByJwtToken = JwtUtils.getMemberIdByJwtToken(request);
        boolean buyCourse = ordersClient.isBuyCourse(courseId, memberIdByJwtToken);
        return R.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoList).data("isBuy",buyCourse);
    }

    //根据课程id获取课程信息
    @PostMapping("getCourseInfoOrder/{id}")
    //为了取值方便，不返回R，而是返回在comment中的ordervo里的对象
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable String id){
        //因为是webvo对象，所以不能用后台的数据，所以也就不能用getbyid
        // EduCourse course = courseService.getById(id);
        CourseWebVo CourseInfo = courseService.getBaseCourseInfo(id);
        //把CourseInfo变成courseWebVoOrder
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(CourseInfo,courseWebVoOrder);
        return courseWebVoOrder;
    }
}

