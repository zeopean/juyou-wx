package com.juyou.test;

import com.juyou.wx.service.IUserService;
import com.juyou.wx.service.card.ICoreService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WxCardTests extends AppApplicationTests {

    @Autowired
    private ICoreService cardService;

    @Autowired
    private IUserService userService;


    @Test
    public void cardHandler()
    {
        WxMpXmlMessage wxMpXmlMessage = new WxMpXmlMessage();
        wxMpXmlMessage.setEventKey("jy_1_88888806");
        wxMpXmlMessage.setFromUser("of7CltyDrkr4pxK8dCwSBSl_bQQc");
        wxMpXmlMessage.setMsgType("event");
        wxMpXmlMessage.setEvent("subscribe");
        cardService.wxEventHandler(wxMpXmlMessage, "default");

    }

    @Test
    public void clickEvent()
    {
        WxMpXmlMessage wxMpXmlMessage = new WxMpXmlMessage();
        wxMpXmlMessage.setEventKey("jy_1");
        wxMpXmlMessage.setFromUser("of7CltyDrkr4pxK8dCwSBSl_bQQc");
        wxMpXmlMessage.setMsgType("event");
        wxMpXmlMessage.setEvent(WxConsts.EventType.CLICK);
        cardService.wxEventHandler(wxMpXmlMessage, "default");

    }

    @Test
    public void userCreate()
    {
        userService.refreshWxUser(null,"of7CltyDrkr4pxK8dCwSBSl_bQQc", "default");
    }

}
