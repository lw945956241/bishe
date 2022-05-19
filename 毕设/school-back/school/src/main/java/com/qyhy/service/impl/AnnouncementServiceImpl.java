package com.qyhy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qyhy.entity.Announcement;
import com.qyhy.mapper.AnnouncementMapper;
import com.qyhy.service.AnnouncementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 公告表 服务实现类
 * </p>
 *
 * @author qyhy
 * @since 2021-03-20
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    public Page<Announcement> queryAllAnnouncement(int currentPage, int pageSize, String name, String type, String createName) {
        Page<Announcement> announcementPage = new Page<>(currentPage, pageSize);
        QueryWrapper<Announcement> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(name), "name", name)
                .like(!StringUtils.isEmpty(createName), "create_name", createName)
                .eq(!StringUtils.isEmpty(type), "type", type);
        return announcementMapper.selectPage(announcementPage, queryWrapper);
    }

    @Override
    public int insert(Announcement announcement) {
        return announcementMapper.insert(announcement);
    }

    @Override
    public int delete(Integer id) {
        return announcementMapper.deleteById(id);
    }

    @Override
    public int update(Announcement announcement) {
        return announcementMapper.updateById(announcement);
    }

}
