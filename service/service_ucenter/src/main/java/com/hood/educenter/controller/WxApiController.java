package com.hood.educenter.controller;


import com.google.gson.Gson;
import com.hood.commonutils.JwtUtils;
import com.hood.educenter.entity.UcenterMember;
import com.hood.educenter.service.UcenterMemberService;
import com.hood.educenter.utils.ConstantWxUtils;
import com.hood.educenter.utils.HttpClientUtils;
import com.hood.servicebase.exceptionhandler.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLEncoder;
import java.util.HashMap;

@Controller//只是请求地址，不需要返回，故不用Restcontroller
@RequestMapping("/api/ucenter/wx")
//@CrossOrigin
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;


    //获取扫描人信息，添加数据.callback在这里是固定的，因为没有自己的域名
    @GetMapping("callback")
    public String callback(String code,String state){
        try{
            //获取code值，临时票据，类似于验证码
            //拿着code请求微信固定地址，得到2个值access_token,和openid
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            //拼接三个参数 ：id  秘钥 和 code值
            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code
            );
            //请求这个拼接好的地址，得到返回两个值access_token,和openid
            //使用httpclient发送请求，得到返回结果
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);

            //从accessTokenInfo字符串中截取access_token,和openid,需要放在map中方便截取
            //用Gson工具做到，之前还有jsonfast
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String)mapAccessToken.get("access_token");
            String openid = (String)mapAccessToken.get("openid");

            //判断是否有重复之后，将扫码人信息添加到数据库，自动注册，
            UcenterMember member = memberService.getOpenIdMember(openid);
            if (member == null){//如果没有，就往数据库里添加

                //拿着得到的access_token和openid，再去请求一个微信的固定地址，得到扫码入信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //拼接两个参数
                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                        access_token,
                        openid
                );
                //发送请求
                String userInfo = HttpClientUtils.get(userInfoUrl);
                //获取返回userinfo字符串扫描人信息
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String)userInfoMap.get("nickname");//昵称
                String headimgurl = (String)userInfoMap.get("headimgurl");//头像
                member = new UcenterMember();
                member.setOpenid(openid);
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                memberService.save(member);
            }

            //使用JWT，根据member对象生成token
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            //最后：返回首页面
            return "redirect:http://localhost:3000?token="+jwtToken;
        }catch (Exception e){
            throw new MyException(20001,"登录失败");
        }
    }

    //生成微信扫描的二维码
    @GetMapping("login")
    public String getWxCode(){

        //固定的地址后面拼接参数
        // 微信开放平台授权baseUrl  %s相当于?代表占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                //原样传递state
                "&state=%s" +
                "#wechat_redirect";

        //对redirect_url进行URLEncoder编码
        String redirectUrl = ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try{
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        }catch (Exception e){

        }

        //设置%s里面值
        String url = String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                "atguigu"
        );
        //重定向到请求微信地址里面
        return "redirect:"+url;
    }
}
