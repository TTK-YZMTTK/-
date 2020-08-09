package com.hood.eduservice.entity.vo;

import lombok.Data;

//将查询选项封装成实体，通过传递实体到接口中，实现分页组合查询
@Data//lombok自动生成实体
public class CourseQuery {

    private String title;

    private Integer status;

}
