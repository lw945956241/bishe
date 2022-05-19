package com.qyhy.interceptor;

import com.qyhy.entity.Session;
import com.qyhy.entity.User;
import com.qyhy.mapper.UserMapper;
import com.qyhy.service.SessionService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoginHandlerInterceptor.class);

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取token
        String token = request.getHeader("token");
        User user = null;
        if(StringUtils.isEmpty(token)){

            Session session = sessionService.getSession(request);

            if (session == null) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                logger.info("session信息为空");
                return false;
            }
            //获取用户从session中
            user = userMapper.queryBySessionId(session.getUserId());

            //如果用户不存在
            if (user == null) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                logger.info("用户不存在");
                return false;
            }
        }
//        else {
//        获取token的一些操作
//
//        }
        request.setAttribute("session.user", user);
        logger.info("得到的用户的id：" + user.getId());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
