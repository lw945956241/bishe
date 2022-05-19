package com.qyhy.controller;


import com.alibaba.fastjson.JSON;
import com.qyhy.entity.Comment;
import com.qyhy.entity.User;
import com.qyhy.mapper.CommentMapper;
import com.qyhy.service.CommentService;
import com.qyhy.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author qyhy
 * @since 2021-03-20
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

    /**
     * 评论某个动态
     *
     * @param comment
     * @return
     */
    @RequestMapping("/comment")
    public String comment(@RequestBody Comment comment,@RequestAttribute(value = "session.user") User loginUser) {
        comment.setCommentName(loginUser.getName());
        int result = commentService.comment(comment);
        if (result > 0) {
            List<Comment> commentList = commentService.getCommentByTalkId(comment);
            return JSON.toJSONString(new Result<>("200", "更新成功,获得该动态的所有评论", commentList));
        }
        return JSON.toJSONString(new Result<>("201", "更新失败", result));
    }

    /**
     * 获取某个动态的评论
     *
     * @param comment
     * @return
     */
    @RequestMapping("/getCommentByTalkId")
    public String getCommentByTalkId(@RequestBody Comment comment) {
        List<Comment> commentList = commentService.getCommentByTalkId(comment);
        return JSON.toJSONString(new Result<>("200", "获得该动态的所有评论", commentList));
    }

    /**
     * 删除评论
     *
     * @param comment
     * @return
     */
    @RequestMapping("/delete")
    public String delete(@RequestBody Comment comment) {
        int result = commentService.delete(comment);
        return JSON.toJSONString(new Result<>("200", "删除评论成功", result));
    }

    @RequestMapping("/getAllComment")
    public String getAllComment() {
        List<Comment> commentList = commentMapper.selectList(null);
        return JSON.toJSONString(new Result<>("200", "获取所有评论成功", commentList));
    }


}

