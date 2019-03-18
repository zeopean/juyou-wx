package com.juyou.wx.service.card;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-08-06
 */
public interface ICoreService {

    /**
     * 用户发送 微信 事件 处理
     *
     * @param inMessage
     * @param wxCode
     */
    WxMpXmlOutMessage wxEventHandler(WxMpXmlMessage inMessage, String wxCode);

    /**
     * 用户发送 文本 消息 处理
     *
     * @param inMessage
     * @param wxCode
     */
    WxMpXmlOutMessage wxTextHandler(WxMpXmlMessage inMessage, String wxCode);

}
