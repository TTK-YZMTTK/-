package com.hood.educenter.service;

import com.hood.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hood.educenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-07-21
 */
public interface UcenterMemberService extends IService<UcenterMember> {
    //用户登录的方法
    String login(UcenterMember member);

    //新用户注册
    void register(RegisterVo registerVo);

    //根据openid判断是否有重复微信id
    UcenterMember getOpenIdMember(String openid);

    //查询某一天的注册人数，也可以返回int，因为只是看count
    Integer countRegisterDay(String day);
}
