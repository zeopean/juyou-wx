package com.juyou.wx.controller;

import com.juyou.wx.config.wx.WxInstance;
import com.juyou.wx.service.card.ICoreService;
import com.juyou.wx.util.logger.LogUtil;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-09-20
 */
@RestController
@RequestMapping("/wxmsg/notify")
public class WxMsgNotifyController {

    @Autowired
    private WxInstance wxInstance;

    @Autowired
    private ICoreService coreService;

    @RequestMapping("/post/{wxCode}")
    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(@RequestBody(required = false) String requestBody,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam(value = "echostr", required = false) String echostr,
                       @RequestParam("nonce") String nonce,
                       @PathVariable("wxCode") String wxCode,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature) {

        final WxMpService wxService = wxInstance.wxMpService(wxCode);

        LogUtil.info(requestBody);

        if (!wxService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        if (StringUtils.isNotBlank(echostr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            return echostr;
        }

        String out = null;
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = this.route(inMessage, wxCode);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toXml();
        } else if ("aes".equalsIgnoreCase(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxService.getWxMpConfigStorage(), timestamp, nonce, msgSignature);

            WxMpXmlOutMessage outMessage = this.route(inMessage, wxCode);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
        }

        LogUtil.info(out);

        return out;
    }

    private WxMpXmlOutMessage route(WxMpXmlMessage inMessage, String wxCode) {
        try {
            // 处理事件消息
            if (WxConsts.XmlMsgType.EVENT.equals(inMessage.getMsgType())) {
                return coreService.wxEventHandler(inMessage, wxCode);
            }

            if (WxConsts.XmlMsgType.TEXT.equals(inMessage.getMsgType())) {
                return coreService.wxTextHandler(inMessage, wxCode);
            }
        } catch (Exception e) {
            LogUtil.error(e, 0, "Exception");
        }

        return null;
    }

}
