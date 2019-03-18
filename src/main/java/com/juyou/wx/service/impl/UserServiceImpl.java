package com.juyou.wx.service.impl;

import com.juyou.wx.common.constants.CacheKey;
import com.juyou.wx.config.wx.WxInstance;
import com.juyou.wx.entity.dao.IUserDao;
import com.juyou.wx.entity.dto.User;
import com.juyou.wx.service.IUserService;
import com.juyou.wx.service.card.helper.CardHelper;
import com.juyou.wx.util.cache.CacheExpiredUtil;
import com.juyou.wx.util.cache.RedisService;
import com.juyou.wx.util.logger.LogUtil;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-09-22
 */
@Primary
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private WxInstance wxInstance;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private RedisService redisService;

    @Override
    public User findOneByOpenid(String openid) {
        String cacheKey = String.format(CacheKey.WX_USER_ONE_OPENID, openid);
        User  user = redisService.get(cacheKey, User.class);
        if (null == user) {
            user = userDao.findOneByOpenid(openid);

            if (null != user) {
                redisService.set(cacheKey, user, CacheExpiredUtil.getHour(2));
            }
        }
        return user;
    }

    @Override
    public User findOneById(long id) {
        String cacheKey = String.format(CacheKey.WX_USER_ONE_ID, id);
        User  user = redisService.get(cacheKey, User.class);
        if (null == user) {
            user = userDao.selectById(id);

            if (null != user) {
                redisService.set(cacheKey, user, CacheExpiredUtil.getHour(2));
            }
        }
        return user;
    }

    @Override
    public User refreshWxUser(User user, String openid, String wxCode) {
        try {
            WxMpService wxMpService = wxInstance.wxMpService(wxCode);
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
            wxMpOAuth2AccessToken.setAccessToken(wxMpService.getAccessToken());
            wxMpOAuth2AccessToken.setOpenId(openid);

            WxMpUser wxMpUser = wxMpService.getUserService().userInfo(openid, null);
            if (null == wxMpUser) {
                return null;
            }

            if (null == user) {
                user = new User();
                user.setOpenid(wxMpUser.getOpenId());
                user.setCreateTime(new Date());
                user.setSubscribeTime(new Date());
            }

            user.setAvatar(wxMpUser.getHeadImgUrl());
            user.setCity(wxMpUser.getCity());
            user.setCountry(wxMpUser.getCountry());
            user.setGender(wxMpUser.getSex());
            user.setNickname( wxMpUser.getNickname());
            user.setProvince(wxMpUser.getProvince());
            user.setUnionid(wxMpUser.getUnionId());
            user.setSubscribe(wxMpUser.getSubscribe() ? 1 : 0);
            user.setUpdateTime(new Date());


            // 更新并清除缓存
            if (userDao.insertOrUpdate(user)) {

                String cacheKey = String.format(CacheKey.WX_USER_ONE_OPENID, openid);
                redisService.remove(cacheKey);

                cacheKey = String.format(CacheKey.WX_USER_ONE_ID, user.getId());
                redisService.remove(cacheKey);

            }

        } catch (WxErrorException e) {
            LogUtil.error(e, 0, "WxErrorException");
        }
        return user;
    }

    @Override
    public User eventUserHandler(WxMpXmlMessage inMessage, String wxCode, boolean reFresh) {
        User user = findOneByOpenid(inMessage.getFromUser());

        if (null == user || reFresh) {
            // 创建活动用户
            user = refreshWxUser(user, inMessage.getFromUser(), wxCode);
        }
        return user;
    }

    @Override
    public User getUserByEventKey(String eventKey) {
        long userId = CardHelper.getUserIdBySceneStr(eventKey);
        return userDao.selectById(userId);
    }

    @Override
    public User unsubscribe(User user) {
        if (null == user) {
            return null;
        }
        user.setSubscribe(0);
        user.setUpdateTime(new Date());
        boolean res = userDao.updateById(user);
        if (res) {
            String cacheKey = String.format(CacheKey.WX_USER_ONE_OPENID, user.getOpenid());
            redisService.remove(cacheKey);

            cacheKey = String.format(CacheKey.WX_USER_ONE_ID, user.getId());
            redisService.remove(cacheKey);
        }
        return user;
    }
}
