package com.juyou.wx.service.card;

import com.juyou.wx.service.card.bean.Msg;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-10-01
 */
public interface IXmlOutMsgService {

    /**
     * 构造文本 xml 消息
     *
     * @param msg
     * @param inMessage
     * @return
     */
    WxMpXmlOutTextMessage textMsgConstruct(Msg msg, WxMpXmlMessage inMessage);



}
