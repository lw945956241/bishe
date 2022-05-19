package com.qyhy.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qyhy.entity.Announcement;
import com.qyhy.entity.User;
import com.qyhy.mapper.AnnouncementMapper;
import com.qyhy.service.AnnouncementService;
import com.qyhy.service.UserService;
import com.qyhy.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 公告表 前端控制器
 * </p>
 *
 * @author qyhy
 * @since 2021-03-20
 */
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AnnouncementMapper announcementMapper;

    @RequestMapping("/queryAllAnnouncement")
    public String queryAllAnnouncement(int currentPage, int pageSize, String name, String type, String createName) {
        Page<Announcement> announcementPage = announcementService.queryAllAnnouncement(currentPage, pageSize, name, type, createName);
        return JSON.toJSONString(new Result<>("200", "查询公告成功", announcementPage));
    }

    @RequestMapping("/getAllAnnouncement")
    public String getAllAnnouncement() {
        QueryWrapper<Announcement> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        List<Announcement> announcements = announcementMapper.selectList(queryWrapper);
        return JSON.toJSONString(new Result<>("200", "查询所有公告成功", announcements));
    }


    @RequestMapping("/insert")
    public String insert(@RequestBody Announcement announcement, @RequestAttribute(value = "session.user") User loginUser) {
        announcement.setCreateName(loginUser.getName());
        announcement.setCreateId(loginUser.getId().toString());
        int result = announcementService.insert(announcement);
        return JSON.toJSONString(new Result<>("200", "插入公告成功", result));
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        int result = announcementService.delete(id);
        return JSON.toJSONString(new Result<>("200", "删除公告成功", result));
    }

    @RequestMapping("/update")
    public String update(@RequestBody Announcement announcement) {
        int result = announcementService.update(announcement);
        return JSON.toJSONString(new Result<>("200", "更新公告成功", result));
    }

    @RequestMapping("/getAnnouncement/{id}")
    public String getAnnouncement(@PathVariable("id") Integer id) {
        Announcement announcement = announcementService.getById(id);
        return JSON.toJSONString(new Result<>("200", "获得公告成功", announcement));
    }


}

