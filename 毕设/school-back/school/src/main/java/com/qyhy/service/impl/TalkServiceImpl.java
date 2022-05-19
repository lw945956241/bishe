package com.qyhy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qyhy.entity.Talk;
import com.qyhy.mapper.TalkMapper;
import com.qyhy.service.TalkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 动态表 服务实现类
 * </p>
 *
 * @author qyhy
 * @since 2021-03-20
 */
@Service
public class TalkServiceImpl extends ServiceImpl<TalkMapper, Talk> implements TalkService {

    @Autowired
    private TalkMapper talkMapper;

    @Override
    public int insert(Talk talk) {
        return talkMapper.insert(talk);
    }

    @Override
    public int delete(Talk talk) {
        return talkMapper.deleteById(talk.getId());
    }

    @Override
    public List<Talk> getTalkByCreateName(Talk talk) {
        QueryWrapper<Talk> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("create_name", talk.getCreateName());
        return talkMapper.selectList(queryWrapper);
    }

    @Override
    public List<Talk> getAllTalk() {
        QueryWrapper<Talk> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        return talkMapper.selectList(queryWrapper);
    }
}
