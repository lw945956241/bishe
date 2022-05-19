package com.qyhy.service;

import com.qyhy.entity.Session;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qyhy.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qyhy
 * @since 2021-03-23
 */
public interface SessionService extends IService<Session> {

    Session getSession(HttpServletRequest request);

    void signOut(User loginUser);

}
