package com.juyou.wx.service;

import com.juyou.wx.entity.dto.User;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-09-22
 */
public interface IUserService {

    /**
     * 通过 openid 查找用户
     *
     * @param openid
     * @return
     */
    User findOneByOpenid(String openid);

    /**
     * 通过 id 查找
     *
     * @param id
     * @return
     */
    User findOneById(long id);

    /**
     * 创建新用户
     *
     * @param openid
     * @return
     */
    User refreshWxUser(User user, String openid, String wxCode);

    /**
     * 事件用户处理
     *
     * @param inMessage
     * @param wxCode
     * @return
     */
    User eventUserHandler(WxMpXmlMessage inMessage, String wxCode, boolean reFresh);

    /**
     * 获取用户事件 key
     *
     * @param eventKey
     * @return
     */
    User getUserByEventKey(String eventKey);

    /**
     * 取消关注
     *
     * @param user
     * @return
     */
    User unsubscribe(User user);

}
