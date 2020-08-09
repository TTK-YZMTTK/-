package com.hood.eduservice.service;

import com.hood.eduservice.entity.EduComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hood.eduservice.entity.vo.Commentvo;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-07-25
 */
public interface EduCommentService extends IService<EduComment> {

    //添加评论的方法
    int addcomment(Commentvo commentvo);
}
