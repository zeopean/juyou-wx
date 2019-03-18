package com.juyou.wx.service.card.impl;

import com.juyou.wx.service.card.IXmlOutMsgService;
import com.juyou.wx.service.card.bean.Msg;
import com.juyou.wx.util.DateTimeUtil;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-10-01
 */
@Primary
@Service
public class XmlOutMsgServiceImpl implements IXmlOutMsgService {

    @Override
    public WxMpXmlOutTextMessage textMsgConstruct(Msg msg, WxMpXmlMessage inMessage) {
        WxMpXmlOutTextMessage wxMpXmlOutMessage = new WxMpXmlOutTextMessage();
        wxMpXmlOutMessage.setContent(msg.getContent());
        wxMpXmlOutMessage.setToUserName(inMessage.getFromUser());
        wxMpXmlOutMessage.setFromUserName(inMessage.getToUser());
        wxMpXmlOutMessage.setCreateTime(DateTimeUtil.dayFormatDateToLong(new Date()));
        return wxMpXmlOutMessage;
    }
}
