package com.hood.eduservice.entity.vo;


import lombok.Data;

@Data
//用于封装课程发布的显示对象
public class CoursePublishVo {
    private String id;
    private String title;
    private String cover;
    private Integer lessonNum;
    private String subjectLevelOne;
    private String subjectLevelTwo;
    private String teacherName;
    private String price;


}
