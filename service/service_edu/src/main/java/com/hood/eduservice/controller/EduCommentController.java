package com.hood.eduservice.controller;


import com.hood.commonutils.R;
import com.hood.eduservice.entity.EduComment;
import com.hood.eduservice.entity.vo.Commentvo;
import com.hood.eduservice.service.EduCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-07-25
 */
@RestController
//@CrossOrigin
@RequestMapping("/eduservice/comment")
public class EduCommentController {

    @Autowired
    private EduCommentService commentService;


    //添加评论的方法
    @PostMapping("addComment")
    public R addComment(@RequestBody Commentvo commentvo){
       int id = commentService.addcomment(commentvo);
        return R.ok().data("id",id);
    }
}

