package com.juyou.wx.service.card;

import com.juyou.wx.entity.dto.User;
import com.juyou.wx.service.card.bean.CardConfig;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-08-29
 */
public interface IImageService {

    /**
     * 获取图片的 微信 mediaId
     *
     * @param inMessage
     * @param wxCode
     * @param scanner
     * @param cardConfig
     * @return
     */
    String getMediaId(WxMpXmlMessage inMessage, String wxCode, User scanner, CardConfig cardConfig);
}
