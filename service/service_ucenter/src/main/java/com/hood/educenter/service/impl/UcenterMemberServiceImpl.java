package com.hood.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hood.commonutils.JwtUtils;
import com.hood.commonutils.MD5;
import com.hood.educenter.entity.UcenterMember;
import com.hood.educenter.entity.vo.RegisterVo;
import com.hood.educenter.mapper.UcenterMemberMapper;
import com.hood.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hood.servicebase.exceptionhandler.MyException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-07-21
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //用户登录
    @Override
    public String login(UcenterMember member) {
        //获取登录的手机号与密码.
        String mobile = member.getMobile();
        String password = member.getPassword();
        //进行判断手机号与密码是否为空
        if(StringUtils.isEmpty(mobile) ||StringUtils.isEmpty(password)){
                throw new MyException(20001,"存在登录信息为空");
        }
        //判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);
        //判断数据库里是否存在该手机号
        if(mobileMember ==null){
            throw new MyException(20001,"登录失败，该手机号不存在或密码错误");
        }
        //判断密码是否符合，数据库中的密码是用MD5加密的，无法解密，需要对输入的密码加密MD5.encrypt后与数据库中比较
        if(!MD5.encrypt(password).equals(mobileMember.getPassword())){
            throw new MyException(20001,"登录失败，该手机号不存在或密码错误");
        }
        //判断用户是否被禁用
        if(mobileMember.getIsDisabled()){
            throw new MyException(20001,"该用户存在违规，已被禁用");
        }


        //登录成功后，用jwt生成token，包装用户信息,得传你数据库中查到的那个对象，传member的话只有账号和密码2个值
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());
        return jwtToken;
    }


    //新用户注册
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册的数据，这是注册者在前台写的信息
        String code = registerVo.getCode();//获取验证码
        String mobile = registerVo.getMobile();//获取手机号
        String nickname = registerVo.getNickname();//获取昵称
        String password = registerVo.getPassword();//获取密码

        //注册非空判断
        if(StringUtils.isEmpty(code) ||StringUtils.isEmpty(mobile)
                ||StringUtils.isEmpty(nickname)||StringUtils.isEmpty(password) ){
            throw new MyException(20001,"存在注册信息为空");
        }
        //判断手机验证码是否正确
        String rediscode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(rediscode)){
            throw new MyException(20001,"验证码输入错误");
        }
        //判断注册信息是否重复
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count >= 1){
            throw new MyException(20001,"该手机已注册");
        }
        //注册成功，添加到数据库
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("https://guli-file-190513.oss-cn-beijing.aliyuncs.com/avatar/default.jpg");
        baseMapper.insert(member);





    }
    //根据openid判断数据库是否有重复微信id
    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    //查询某一天的注册人数，也可以返回int，因为只是看count，因为用到count（*），故用sql语句更方便实现
    @Override
    public Integer countRegisterDay(String day) {
        return baseMapper.countRegisterDay(day);
    }
}
