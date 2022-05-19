package com.qyhy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qyhy.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qyhy
 * @since 2021-03-14
 */
public interface UserService extends IService<User> {

    //登陆
    User login(User user);

    //注册
    int register(User user);

    //更改用户
    int update(User user);

    //删除用户
    int delete(User user);

    //查询所有用户
    Page<User> queryAllUser(int currentPage, int pageSize, String name, String phone, String mail, String admissionTime, String highestDegree, String work);
}
