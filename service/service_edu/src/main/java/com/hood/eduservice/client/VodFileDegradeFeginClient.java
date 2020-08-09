package com.hood.eduservice.client;

import com.hood.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * VodClient接口的实现类，作用为VodClient出错时调用
 */


@Component
public class VodFileDegradeFeginClient implements VodClient{
    //出错之后才会执行

    @Override
    public R removeAlyVideo(String id) {
        return R.error().message("删除视频出错了");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("删除多个视频出错了");
    }
}
