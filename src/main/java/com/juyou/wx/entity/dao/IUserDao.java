package com.juyou.wx.entity.dao;

import com.baomidou.mybatisplus.service.IService;
import com.juyou.wx.entity.dto.User;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-09-19
 */
public interface IUserDao extends IService<User> {

    /**
     * 通过 openid 查找用户
     *
     * @param openid
     * @return
     */
    User findOneByOpenid(String openid);


}
