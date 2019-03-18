package com.juyou.wx.service.card;

import com.juyou.wx.entity.dto.User;
import com.juyou.wx.service.card.bean.CardConfig;
import com.juyou.wx.service.card.bean.Msg;

import com.juyou.wx.vos.WxKefuMessageVo;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;

import java.util.List;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-09-02
 */
public interface IKefuMsgService {

    /**
     * 文本消息构造器
     *
     * @param wxCode
     * @param openid
     * @param context
     * @return
     */
    WxKefuMessageVo textMsgConstruct(String wxCode, String openid, String context);

    /**
     * 图片消息构造器
     *
     * @param wxCode
     * @param openid
     * @param mediaId
     * @return
     */
    WxKefuMessageVo imgMsgConstruct(String wxCode, String openid, String mediaId);

    /**
     * 图文消息构造
     *
     * @param wxCode
     * @param openid
     * @param wxArticles
     * @return
     */
    WxKefuMessageVo newsMsgConstruct(String wxCode, String openid, List<WxMpKefuMessage.WxArticle> wxArticles);

    /**
     * 小程序 构造
     *
     * @param wxCode
     * @param openid
     * @param title
     * @param appid
     * @param pagePath
     * @return
     */
    WxKefuMessageVo miniappMsgConstruct(String wxCode, String openid, String title, String appid, String pagePath, String imageUrl);

    /**
     * 解锁成功文本消息
     *
     * @param wxCode
     * @param openid
     * @param cardConfig
     * @return
     */
    void receiveSuccessMsgConstruct(String wxCode, String openid, CardConfig cardConfig);

    /**
     * 扫码人消息处理
     *
     * @param scannerMsg
     * @param wxCode
     * @param scanner
     */
    void kefuScannerMsgSend(CardConfig cardConfig, Msg scannerMsg, String wxCode, User scanner, User sharer, String eventType);

    /**
     * 分享人 消息处理
     *
     * @param scannerMsg
     * @param wxCode
     * @param scanner
     */
    void kefuSharerMsgSend(CardConfig cardConfig, Msg scannerMsg, String wxCode, User scanner, User sharer, int needNum);

}
