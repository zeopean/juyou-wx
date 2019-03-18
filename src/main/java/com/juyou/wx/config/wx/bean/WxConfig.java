package com.juyou.wx.config.wx.bean;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-06-25
 */
@Component
@PropertySource("classpath:application-wx.properties")
public class WxConfig implements Serializable {

    protected String appId;
    protected String appSecret;
    protected String mchId;
    protected String appKey;
    protected String encodingToken;
    protected String encodingAesKey;
    protected String notifyUrl;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getEncodingToken() {
        return encodingToken;
    }

    public void setEncodingToken(String encodingToken) {
        this.encodingToken = encodingToken;
    }

    public String getEncodingAesKey() {
        return encodingAesKey;
    }

    public void setEncodingAesKey(String encodingAesKey) {
        this.encodingAesKey = encodingAesKey;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}
