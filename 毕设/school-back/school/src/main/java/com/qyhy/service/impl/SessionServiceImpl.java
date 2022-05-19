package com.qyhy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qyhy.entity.Session;
import com.qyhy.entity.User;
import com.qyhy.mapper.SessionMapper;
import com.qyhy.service.SessionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qyhy
 * @since 2021-03-23
 */
@Service
public class SessionServiceImpl extends ServiceImpl<SessionMapper, Session> implements SessionService {

    @Autowired
    private SessionMapper sessionMapper;

    //通过name获取cookie信息
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equalsIgnoreCase(name, cookie.getName())) {
                    return cookie;
                }
            }
        }

        return null;
    }

    //从request请求中获取用户session
    @Override
    public Session getSession(HttpServletRequest request) {
        String sessionId = request.getHeader("sessionId");

        if (StringUtils.isBlank(sessionId)) {
            Cookie cookie = getCookie(request, "sessionId");

            if (cookie != null) {
                sessionId = cookie.getValue();
            }
        }

        if (StringUtils.isBlank(sessionId)) {
            return null;
        }

        return sessionMapper.selectById(sessionId);
    }

    //退出登录
    @Override
    public void signOut(User loginUser) {
        QueryWrapper<Session> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", loginUser.getId());
        Session session = sessionMapper.selectOne(queryWrapper);
        //delete session
        sessionMapper.deleteById(session.getSessionid());
    }

}
