package com.hood.eduservice.entity.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Commentvo {

    @ApiModelProperty(value = "评论内容")
    private String content;
}
