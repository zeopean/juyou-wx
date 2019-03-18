package com.juyou.wx.vos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;

/**
 * Description: 邀请卡 客服 消息
 *
 * @author zeopean
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxKefuMessageVo {
    private WxMpKefuMessage wxMpKefuMessage;
    private String wxCode;
    private long time;

    public WxKefuMessageVo(WxMpKefuMessage wxMpKefuMessage, String wxCode) {
        this.wxMpKefuMessage = wxMpKefuMessage;
        this.wxCode = wxCode;
        this.time = System.currentTimeMillis();
    }
}
