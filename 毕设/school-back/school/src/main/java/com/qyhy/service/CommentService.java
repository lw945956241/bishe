package com.qyhy.service;

import com.qyhy.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author qyhy
 * @since 2021-03-20
 */
public interface CommentService extends IService<Comment> {
    int comment(Comment comment);

    List<Comment> getCommentByTalkId(Comment comment);

    int delete(Comment comment);

}
