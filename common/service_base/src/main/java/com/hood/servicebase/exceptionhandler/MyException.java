package com.hood.servicebase.exceptionhandler;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data//lombok自动生成set，get
@AllArgsConstructor//lombok自动生成有参构造
@NoArgsConstructor//lombok自动生成无参构造
public class MyException extends RuntimeException{
    private Integer code;//状态码
    private String msg;//异常信息
}