package com.juyou.wx.entity.dao.impl;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.juyou.wx.common.constants.Columns;
import com.juyou.wx.entity.dao.IUserDao;
import com.juyou.wx.entity.dto.User;
import com.juyou.wx.entity.mapper.UserMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 *
 * @author zeopean
 * @since 2018-09-20
 */
@Primary
@Service
public class UserDaoImpl extends ServiceImpl<UserMapper, User> implements IUserDao {

    @Override
    public User findOneByOpenid(String openid) {
        return super.selectOne(new Condition().eq(Columns.openid, openid));
    }
}
