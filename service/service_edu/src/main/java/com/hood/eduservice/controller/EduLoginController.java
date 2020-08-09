package com.hood.eduservice.controller;


import com.hood.commonutils.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduservice/user")
//@CrossOrigin  //解决跨域
public class EduLoginController {
    //login
    @PostMapping("/login")
    public R login() {
        return R.ok().data("token","admin");
    }
    //info
    @GetMapping("/info")
    public R info() {
        return R.ok().data("roles","[admin]").data("name","admin").data("avatar","https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1107169796,4088768198&fm=26&gp=0.jpg");
    }
}
