package com.hood.educenter.mapper;

import com.hood.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-07-21
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    //查询某一天的注册人数，也可以返回int，因为只是看count，因为用到count（*），故用sql语句更方便实现
    Integer countRegisterDay(String day);
}
