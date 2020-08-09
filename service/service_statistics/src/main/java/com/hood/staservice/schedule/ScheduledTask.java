package com.hood.staservice.schedule;


import com.hood.staservice.service.StatisticsDailyService;
import com.hood.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component

public class ScheduledTask {

    @Autowired
    private StatisticsDailyService staService;

    //在每天凌晨的一点执行该方法，把前一天的数据进行查询添加
    //七子，七域，cron表达式，默认当前年，所以只有6位
    @Scheduled(cron = "0 0 1 * * ?")
    public void task1(){
        staService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(),-1)));
    }

}
