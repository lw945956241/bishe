package com.qyhy.service;

import com.qyhy.entity.Talk;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 动态表 服务类
 * </p>
 *
 * @author qyhy
 * @since 2021-03-20
 */
public interface TalkService extends IService<Talk> {

    int insert(Talk talk);

    int delete(Talk talk);

    List<Talk> getTalkByCreateName(Talk talk);

    List<Talk> getAllTalk();

}
