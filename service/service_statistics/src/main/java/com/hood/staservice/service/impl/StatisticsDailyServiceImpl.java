package com.hood.staservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hood.commonutils.R;
import com.hood.staservice.client.UcenterClient;
import com.hood.staservice.entity.StatisticsDaily;
import com.hood.staservice.mapper.StatisticsDailyMapper;
import com.hood.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-07-28
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;


    @Override
    public void registerCount(String day) {
        //添加记录之前，删除表相同日期的数据，防止出现同一天多组不同值得数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        baseMapper.delete(wrapper);


        //远程调用ucenter方法，得到某一天注册人数
        R registerR = ucenterClient.countRegister(day);
        //根据咱们R里面data数据key：value的形式，通过key得到value（count）
        Integer countRegister = (Integer)registerR.getData().get("countRegister");

        //添加到统计分析表中
        StatisticsDaily sta = new StatisticsDaily();
        sta.setRegisterNum(countRegister);//注册人数
        sta.setDateCalculated(day);//统计日期
        //还有一些其他值，在这就直接生成模拟一下
        sta.setCourseNum(RandomUtils.nextInt(100,200));
        sta.setVideoViewNum(RandomUtils.nextInt(100,200));
        sta.setLoginNum(RandomUtils.nextInt(100,200));

        baseMapper.insert(sta);

    }

    //图表显示,返回两部分数据，日期json数组，数量json数组
    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
        //根据条件查询对应的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated",begin,end);
        wrapper.select("date_calculated",type);
        List<StatisticsDaily> staList = baseMapper.selectList(wrapper);
        //返回数据的封装，前段要求的是2分json数组[]结构，对应后端java代码是list集合，{}对应的是对象和map
        List<String> date_calculatedList = new ArrayList<>();  //用来放日期的集合
        List<Integer> numDataList= new ArrayList<>();           //用来放数量的集合
        //遍历查询出来的list集合，向2个list分别封装
        for (int i = 0; i < staList.size(); i++) {
            StatisticsDaily daily = staList.get(i);
            //封装日期的集合
            date_calculatedList.add(daily.getDateCalculated());
            //封装数量的集合，判断type传过来的是那种需求数据，登录，还是注册啥的
            switch(type){
                case "login_num":
                    numDataList.add(daily.getLoginNum());
                    break;
                case "register_num":
                    numDataList.add(daily.getRegisterNum());
                    break;
                case "video_view_num":
                    numDataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    numDataList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }
        //吧封装之后的list集合放在map中进行返回
        Map<String,Object> map = new HashMap<>();
        map.put("date_calculatedList",date_calculatedList);
        map.put("numDataList",numDataList);

        return map;
    }
}
