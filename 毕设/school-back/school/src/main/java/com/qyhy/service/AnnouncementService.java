package com.qyhy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qyhy.entity.Announcement;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 公告表 服务类
 * </p>
 *
 * @author qyhy
 * @since 2021-03-20
 */
public interface AnnouncementService extends IService<Announcement> {

    Page<Announcement> queryAllAnnouncement(int currentPage, int pageSize, String name, String type, String createName);

    int insert(Announcement announcement);

    int delete(Integer id);

    int update(Announcement announcement);

}
