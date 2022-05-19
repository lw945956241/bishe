package com.qyhy.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qyhy.entity.Announcement;
import com.qyhy.entity.Talk;
import com.qyhy.entity.User;
import com.qyhy.mapper.TalkMapper;
import com.qyhy.service.TalkService;
import com.qyhy.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 动态表 前端控制器
 * </p>
 *
 * @author qyhy
 * @since 2021-03-20
 */
@RestController
@RequestMapping("/talk")
public class TalkController {

    @Autowired
    private TalkService talkService;

    @Autowired
    private TalkMapper talkMapper;

    @RequestMapping("/insert")
    public String insert(@RequestBody Talk talk, @RequestAttribute(value = "session.user") User loginUser) {
        talk.setCreateName(loginUser.getName());
        int result = talkService.insert(talk);
        return JSON.toJSONString(new Result<>("200", "发布动态成功", result));
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        boolean remove = talkService.removeById(id);
        return JSON.toJSONString(new Result<>("200", "删除动态成功", remove));
    }

    @RequestMapping("/getTalkByCreateName")
    public String getTalkByCreateName(@RequestBody Talk talk) {
        List<Talk> talkList = talkService.getTalkByCreateName(talk);
        return JSON.toJSONString(new Result<>("200", "获取动态成功", talkList));
    }

    @RequestMapping("/getAllTalk")
    public String getAllTalk() {
        List<Talk> talkList = talkService.getAllTalk();
        return JSON.toJSONString(new Result<>("200", "获取动态成功", talkList));
    }

    @RequestMapping("/getPageTalk")
    public String queryAllAnnouncement(int currentPage, int pageSize, String createName, @RequestAttribute(value = "session.user") User loginUser) {
        Page<Talk> talkPage = new Page<>(currentPage, pageSize);
        QueryWrapper<Talk> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        if ("1".equals(createName)) {
            queryWrapper.eq("create_name", loginUser.getName());
        }
        Page<Talk> talkPage1 = talkMapper.selectPage(talkPage, queryWrapper);
        return JSON.toJSONString(new Result<>("200", "查询公告成功", talkPage1));
    }

}

