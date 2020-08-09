package com.hood.eduorder.service;

import com.hood.eduorder.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-07-26
 */
public interface PayLogService extends IService<PayLog> {

    //生成支付二维码
    Map creatNative(String orderNo);
    //根据订单号，查询订单支付状态
    Map<String, String> queryPayStatus(String orderNo);
    //向支付表添加记录，更新订单状态
    void updateOrdersStatus(Map<String, String> map);
}
