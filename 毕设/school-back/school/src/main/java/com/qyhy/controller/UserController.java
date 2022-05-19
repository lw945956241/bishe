package com.qyhy.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qyhy.entity.Session;
import com.qyhy.entity.User;
import com.qyhy.mapper.SessionMapper;
import com.qyhy.service.SessionService;
import com.qyhy.service.UserService;
import com.qyhy.utils.Result;
import com.qyhy.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qyhy
 * @since 2021-03-14
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private SessionService sessionService;

    /**
     * 用户登陆
     *
     * @param user
     * @return
     */
    @RequestMapping("/login")
    public String login(@RequestBody User user, HttpServletResponse response) {
        User loginUser = userService.login(user);
        if (loginUser == null) {
            return JSON.toJSONString(new Result<>("202", "没有该用户", null));
        }
        if (!loginUser.getPassword().equals(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()))) {
            return JSON.toJSONString(new Result<>("201", "密码不正确", null));
        }
        QueryWrapper<Session> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", loginUser.getId());
        Session session = sessionMapper.selectOne(queryWrapper);
        Date now = new Date();

        //如果有该用户的session
        if (session != null) {
            //还在session失效时间之内
            if (now.getTime() - session.getLoginTime().toInstant(ZoneOffset.of("+8")).toEpochMilli() <= 7200 * 1000) {
                //更新登录时间
                session.setLoginTime(now.toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime());
                UpdateWrapper updateWrapper = new UpdateWrapper();
                sessionMapper.updateById(session);
            } else {
                //如果已超过时间，先删了该session
                sessionMapper.deleteById(session.getSessionid());
            }
        } else {
            //没有的话创建新的session
            session = new Session();
            session.setSessionid(UUID.randomUUID().toString());
            session.setUserId(loginUser.getId());
            session.setLoginTime(now.toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime());
            sessionMapper.insert(session);
        }
        String sessionId = session.getSessionid();
        UserVo userVo = new UserVo();
        userVo.setId(loginUser.getId())
                .setName(loginUser.getName())
                .setPassword(loginUser.getPassword())
                .setPhone(loginUser.getPhone())
                .setMail(loginUser.getMail())
                .setType(loginUser.getType())
                .setCreateTime(loginUser.getCreateTime())
                .setUpdateTime(loginUser.getUpdateTime())
                .setAdmissionTime(loginUser.getAdmissionTime())
                .setHighestDegree(loginUser.getHighestDegree())
                .setWork(loginUser.getWork())
                .setSessionId(sessionId);
        response.addCookie(new Cookie("sessionId", sessionId));
        return JSON.toJSONString(new Result<>("200", "成功", userVo));
    }

    @RequestMapping("/register")
    public String register(@RequestBody User user) {
        int result = userService.register(user);
        if (result == -1) {
            return JSON.toJSONString(new Result<>("201", "用户名已存在", result));
        }
        return JSON.toJSONString(new Result<>("200", "注册成功", result));
    }

    @RequestMapping("/update")
    public String update(Integer id, String name, String phone, String mail, String admissionTime, String highestDegree, String work) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        User user = new User();
        user.setName(name)
                .setId(id)
                .setPhone(phone)
                .setMail(mail)
                .setAdmissionTime(LocalDateTime.parse(admissionTime, df))
                .setHighestDegree(highestDegree)
                .setWork(work);
        int result = userService.update(user);
        return JSON.toJSONString(new Result<>("200", "更新成功", result));
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        boolean remove = userService.removeById(id);
        return JSON.toJSONString(new Result<>("200", "删除成功", remove));
    }

    @RequestMapping("/queryAllUser")
    public String queryAllUser(int currentPage, int pageSize, String name, String phone, String mail, String admissionTime, String highestDegree, String work) {
        Page<User> userList = userService.queryAllUser(currentPage, pageSize, name, phone, mail, admissionTime, highestDegree, work);
        return JSON.toJSONString(new Result<>("200", "查询用户成功", userList));
    }

    @GetMapping("/getLoginUser")
    public String getLoginUser(@RequestAttribute(value = "session.user") User loginUser) {
        return JSON.toJSONString(new Result<>("200", "查询用户", loginUser));
    }

    //退出登录
    @PostMapping(value = "/signOut")
    public String signOut(@RequestAttribute(value = "session.user") User loginUser,
                          HttpServletRequest request) {

        try {
            sessionService.signOut(loginUser);
            request.removeAttribute("session.user");
            return JSON.toJSONString(new Result<>("200", "退出成功", null));
        } catch (Exception e) {
            return JSON.toJSONString(new Result<>("201", "退出失败", null));
        }
    }

    @PostMapping("/modifyPassword")
    public String modifyPassword(@RequestBody JSONObject json, @RequestAttribute(value = "session.user") User loginUser) {
        String oldPassword = json.getString("oldPassword");
        if (!loginUser.getPassword().equals(DigestUtils.md5DigestAsHex(oldPassword.getBytes()))) {
            return JSON.toJSONString(new Result<>("201", "旧密码不一致", null));
        }
        String newPassword = json.getString("newPassword");
        User newUser = new User();
        newUser.setId(loginUser.getId()).setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
        userService.update(newUser);
        return JSON.toJSONString(new Result<>("200", "修改密码成功", loginUser));
    }


}

