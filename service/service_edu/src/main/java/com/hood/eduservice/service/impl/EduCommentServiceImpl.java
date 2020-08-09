package com.hood.eduservice.service.impl;

import com.hood.eduservice.entity.EduComment;
import com.hood.eduservice.entity.vo.Commentvo;
import com.hood.eduservice.mapper.EduCommentMapper;
import com.hood.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-07-25
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {

    //添加评论的方法
    @Override
    public int addcomment(Commentvo commentvo) {
        EduComment eduComment = new EduComment();
        BeanUtils.copyProperties(commentvo,eduComment);
        int insert = baseMapper.insert(eduComment);
        return insert;
    }
}

