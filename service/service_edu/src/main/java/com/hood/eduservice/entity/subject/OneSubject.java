package com.hood.eduservice.entity.subject;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//一级分类
@Data
public class OneSubject {
    private String id;

    private String title;

    //一级分类可以包含多个二级分类，故用List
    private List<TwoSubject> children = new ArrayList<>();
}
