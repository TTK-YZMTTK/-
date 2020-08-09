package com.hood.eduservice.entity.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

//将查询选项封装成实体，通过传递实体到接口中，实现分页组合查询
@Data//lombok自动生成实体
public class TeacherQuery {

    @ApiModelProperty(value = "教师名称，模糊查询")
    private String name;

    @ApiModelProperty(value = "教师头衔 1高级讲师，2首席讲师")
    private Integer level;

    @ApiModelProperty(value = "查询开始时间",example = "2019-01-01 10:10:10")
    private String begin;

    @ApiModelProperty(value = "查询结束时间",example = "2019-12-01 10:10:10")
    private String end;
}
