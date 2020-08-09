package com.hood.eduservice.controller;


import com.hood.commonutils.R;
import com.hood.eduservice.entity.EduTeacher;
import com.hood.eduservice.entity.vo.TeacherQuery;
import com.hood.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-05-17
 */
@Api(tags = "讲师管理")  //swagger中文提示
@RestController
//@CrossOrigin  //解决跨域问题
@RequestMapping("/eduservice/teacher")
public class EduTeacherController {
    //把service注入
    @Autowired
    private EduTeacherService teacherService;

    //1 查询讲师表所有数据
    //rest风格
    @ApiOperation(value = "所有讲师列表")//swagger中文提示
    @GetMapping("findAll")
    public R findAllTeacher(){
        //调用service的方法实现查询所有的操作
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items",list);
    }

    //2逻辑删除讲师的方法
    @ApiOperation(value = "逻辑删除")//swagger中文提示
    @DeleteMapping("{id}")//ID需要路径进行传递
    public R removeTeacher(@ApiParam(name = "id",value = "讲师ID",required = true)@PathVariable String id){
        boolean flag = teacherService.removeById(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //3分页查询讲师的方法
    //current代表当前页
    //limit代表每页显示的记录数
    @GetMapping("pageTeacher/{current}/{limit}")
    @ApiOperation(value = "分页查询")//swagger中文提示
    public R pageListTeacher(@PathVariable long current,@PathVariable long limit){
        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);
        //调用page方法实现分页,将数据封装到page方法中
        teacherService.page(pageTeacher,null);
        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords();//数据list集合
//        Map map = new HashMap();
//        map.put("total",total);
//        map.put("records",records);
//        return R.ok().data(map);
        return R.ok().data("total",total).data("records",records);
    }

    //4条件查询带分页的方法
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    @ApiOperation(value = "条件查询带分页")//swagger中文提示
    public R pageTeacherCondition(@PathVariable long current,@PathVariable long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery){
        //(required = false)加上这个里面的值可以为空
        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);
        //创建QueryWrapper对象
        //通过QueryWrapper设置条件
        //ge（大于等于）,gt（大于）,le（小于等于）,lt（小于）
        //eq（等于）,ne（不等于）
        //last在后面拼一条sql语句
        //指定要查的列。 wrapper.select("id","name")
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //多条件组合查询，动态sql
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //判断条件是否为空，如果不为空则拼接条件
        if(!StringUtils.isEmpty(name)){
            //构建条件
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)){
            //构建条件
            wrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)){
            //构建条件
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)){
            //构建条件
            wrapper.le("gmt_create",end);
        }
        //排序
        wrapper.orderByDesc("gmt_create");
        //调用方法实现条件查询带分页功能
        teacherService.page(pageTeacher,wrapper);
        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords();//数据list集合
        return R.ok().data("total",total).data("rows",records);
    }

    //5添加讲师方法的接口
    @PostMapping("addTeacher")
    @ApiOperation(value = "添加讲师")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save = teacherService.save(eduTeacher);
        if(save){
            return R.ok();
        }else {
            return R.error();
        }
    }
    //6根据讲师ID进行查询
    @GetMapping("getTeacher/{id}")
    @ApiOperation(value = "通过ID查询")
    public R getTeacher(@PathVariable String id){
        EduTeacher eduTeacher = teacherService.getById(id);
        return R.ok().data("teacher",eduTeacher);
    }
    //7在6的基础上，实现讲师修改功能
    @PostMapping("updateTeacher")
    @ApiOperation(value = "修改")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean flag = teacherService.updateById(eduTeacher);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

}



