package com.juyou.wx.service.card;

import com.juyou.wx.entity.dto.User;
import com.juyou.wx.service.card.bean.CardConfig;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-10-09
 */
public interface IActivateService {

    /**
     * 创建记录
     *
     * @param inMessage
     * @param scanner
     * @param mediaId
     * @param cardConfig
     * @return
     */
    boolean createInvicardLog(WxMpXmlMessage inMessage, User scanner, String mediaId, CardConfig cardConfig);

    /**
     * 更新记录
     *
     * @param inMessage
     * @param sharer
     * @param cardConfig
     */
    void updateInvicardLog(WxMpXmlMessage inMessage, User sharer, CardConfig cardConfig);
}
