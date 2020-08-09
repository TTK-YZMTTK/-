package com.hood.commonutils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data//lombok自动生成实体
public class R {
    @ApiModelProperty(value = "是否成功")//swagger信息提示
    private Boolean success;

    @ApiModelProperty(value = "返回码")//swagger信息提示
    private Integer code;

    @ApiModelProperty(value = "返回信息")//swagger信息提示
    private String message;

    @ApiModelProperty(value = "返回数据")//swagger信息提示
    private Map<String,Object> data = new HashMap<String,Object>();

    //构造方法私有化
    private R(){}

    //成功的静态方法
    public static R ok() {
        R r = new R();
        r.setCode(ResultCode.SUCCESS);
        r.setMessage("成功");
        r.setSuccess(true);
        return r;
    }

    //失败的静态方法
    public static R error(){
        R r = new R();
        r.setCode(ResultCode.ERROR);
        r.setMessage("失败");
        r.setSuccess(false);
        return r;
    }
    public R success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public R message(String message){
        this.setMessage(message);
        return this;
    }

    public R code(Integer code){
        this.setCode(code);
        return this;
    }

    public R data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public R data(Map<String, Object> map){
        this.setData(map);
        return this;
    }

}
