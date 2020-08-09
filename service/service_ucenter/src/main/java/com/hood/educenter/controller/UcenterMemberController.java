package com.hood.educenter.controller;


import com.hood.commonutils.JwtUtils;
import com.hood.commonutils.R;
import com.hood.commonutils.ordervo.UcenterMemberOrder;
import com.hood.educenter.entity.UcenterMember;
import com.hood.educenter.entity.vo.RegisterVo;
import com.hood.educenter.service.UcenterMemberService;
import com.hood.educenter.service.impl.UcenterMemberServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-07-21
 */
@RestController
@RequestMapping("/educenter/member")
//@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    //用户登录
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember member){
        //调用service中的方法来实现登录,登录之后的用户信息放在jwt生成的token中
        String token = memberService.login(member);
        return R.ok().data("token",token);

    }

    //新用户注册
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }

    //根据token获取用户信息
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        //调用jwt工具类的方法，根据request对象获取头信息，返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库，根据用户id获取用户信息
        UcenterMember member = memberService.getById(memberId);
        return R.ok().data("userInfo",member);
    }

    //根据用户id获取用户信息
    @PostMapping("getUserInfoOrder/{id}")
    //为了取值方便，不返回R，而是返回在comment中的ordervo里的对象
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id){
        UcenterMember member = memberService.getById(id);
        //把member变成memberOrder
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member,ucenterMemberOrder);
        return ucenterMemberOrder;
    }


    //查询某一天的注册人数，也可以返回int，因为只是看count
    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable String day){
       Integer count = memberService.countRegisterDay(day);
       return R.ok().data("countRegister",count);
    }
}

