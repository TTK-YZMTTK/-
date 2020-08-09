package com.hood.eduorder.service;

import com.hood.eduorder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-07-26
 */
public interface OrderService extends IService<Order> {

    //生成订单的方法
    String creatOrders(String courseId, String memberIdByJwtToken);
}
