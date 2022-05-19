package com.qyhy.mapper;

import com.qyhy.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qyhy
 * @since 2021-03-14
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    User queryBySessionId(@Param("sessionId") Integer sessionId);

}
