package com.qyhy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qyhy.entity.Comment;
import com.qyhy.mapper.CommentMapper;
import com.qyhy.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author qyhy
 * @since 2021-03-20
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public int comment(Comment comment) {
        return commentMapper.insert(comment);
    }

    @Override
    public List<Comment> getCommentByTalkId(Comment comment) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("talk_id", comment.getTalkId());
        return commentMapper.selectList(queryWrapper);
    }

    @Override
    public int delete(Comment comment) {
        comment.setContent("该内容已被删除");
        return commentMapper.updateById(comment);
    }

}
