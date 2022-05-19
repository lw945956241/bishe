package com.qyhy.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qyhy.entity.User;
import com.qyhy.mapper.UserMapper;
import com.qyhy.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qyhy
 * @since 2021-03-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登陆
     *
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", user.getName());
        User loginUser = userMapper.selectOne(queryWrapper);
        return loginUser;
    }

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    @Override
    public int register(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", user.getName());
        User registerUser = userMapper.selectOne(queryWrapper);
        if (registerUser != null) {
            return -1;
        }
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setType("1");
        int registerUserResult = userMapper.insert(user);
        return registerUserResult;
    }

    @Override
    public int update(User user) {
        int updateResult = userMapper.updateById(user);
        return updateResult;
    }

    @Override
    public int delete(User user) {
        return userMapper.deleteById(user.getId());
    }

    @Override
    public Page<User> queryAllUser(int currentPage, int pageSize, String name, String phone, String mail, String admissionTime, String highestDegree, String work) {
        Page<User> userPage = new Page<>(currentPage, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isBlank(name), "name", name)
                .like(!StringUtils.isBlank(phone), "phone", phone)
                .like(!StringUtils.isBlank(mail), "mail", mail)
                .eq(!StringUtils.isBlank(highestDegree), "highest_degree", highestDegree)
                .eq(!StringUtils.isBlank(work), "work", work)
                .ge(!StringUtils.isBlank(admissionTime), "admission_time", admissionTime);
        Page<User> userPageResult = userMapper.selectPage(userPage, queryWrapper);
        return userPageResult;
    }

    //每年定时清理一次数据
    @Scheduled(cron = "0 0 0 0 0 ? *")
    public void updateUserPerYear() {
        User user = new User();
        user.setWork("").setAdmissionTime(null).setMail("").setPhone("").setHighestDegree("");
        userMapper.update(user, null);
    }


}
