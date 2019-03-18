package com.juyou.wx.config.wx.bean;

import com.juyou.wx.config.wx.WechatType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-06-25
 */
@Component
@ConfigurationProperties(prefix = "juyou.wx.wxmp")
public class WxMp extends WxConfig {
    private String alias = WechatType.WxMp.getWxCode();

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
