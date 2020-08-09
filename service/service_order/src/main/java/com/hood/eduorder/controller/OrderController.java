package com.hood.eduorder.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hood.commonutils.JwtUtils;
import com.hood.commonutils.R;
import com.hood.eduorder.entity.Order;
import com.hood.eduorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-07-26
 */
@RestController
@RequestMapping("/eduorder/order")
//@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    //添加订单数据(与评论类似)，在课程中购买，所以能把课程id传过来
    @PostMapping("createOrder/{courseId}")
    public R saveOrder(@PathVariable String courseId, HttpServletRequest request){
       // String memberIdByJwtToken = JwtUtils.getMemberIdByJwtToken(request); 得到token中的用户信息

        //创建订单，返回订单号
       String orderNo = orderService.creatOrders(courseId,JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("orderId",orderNo);
    }

    //根据订单id查询订单信息
    @GetMapping("getOrderInfo/{OrderId}")
    public R getOrderInfo(@PathVariable String OrderId){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",OrderId);
        Order order = orderService.getOne(wrapper);
        return R.ok().data("order",order);
    }

    //根据课程id和用户id去查询订单表中的订单状态,可以返回R，但返回boolean
    @GetMapping("isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable String courseId,@PathVariable String memberId){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.eq("member_id",memberId);
        wrapper.eq("status",1);//支付状态
        int count = orderService.count(wrapper);//看看这3个条件能不能查出东西来
        if (count>0){//查到了，status是1，已支付
            return true;
        }else {
            return false;
        }

    }
}

