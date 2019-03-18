package com.juyou.wx.config.wx;

import com.juyou.wx.common.constants.CacheKey;
import com.juyou.wx.common.constants.ResponseData;
import com.juyou.wx.common.constants.WxConstant;
import com.juyou.wx.config.wx.bean.Default;
import com.juyou.wx.config.wx.bean.WxConfig;
import com.juyou.wx.config.wx.bean.WxMp;
import com.juyou.wx.util.GeneralUtil;
import com.juyou.wx.util.cache.CacheExpiredUtil;
import com.juyou.wx.util.cache.RedisService;
import com.juyou.wx.util.logger.LogUtil;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-06-26
 */
@Component
public class WxInstance {

    @Resource
    protected RedisService cache;

    @Resource
    Default aDefault;

    @Resource
    WxMp wxMp;

    /**
     *
     * @param wxCode
     * @return
     */
    public WxConfig getInstance(String wxCode) {

        if (WechatType.Default.getWxCode().equals(wxCode)) {
            return aDefault;
        }

        if (WechatType.WxMp.getWxCode().equals(wxCode)) {
            return wxMp;
        }

        return null;
    }



    public WxMpService wxMpService(String wxCode) {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpConfigStorage wxMpConfigStorage = this.wxMpConfigStorage(wxCode);
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage);

        try {
            WxConfig wxConfig = getInstance(wxCode);
            String cacheKey = String.format(CacheKey.WXAPI_ACCESS_TOKEN_APPID, wxConfig.getAppId());
            String token = cache.get(cacheKey, String.class);

            if (GeneralUtil.isObjNull(token)) {
                token = wxMpService.getAccessToken(true);

                if (GeneralUtil.isObjNotNull(token)) {
                    cache.set(cacheKey, token, CacheExpiredUtil.getSecond(WxConstant.REAL_TOKEN_EXPIRED));
                }
            }
            if (GeneralUtil.isObjNotNull(token)) {
                wxMpConfigStorage.updateAccessToken(token, WxConstant.TOKEN_EXPIRED);
                wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
            }

        } catch (WxErrorException e) {
            LogUtil.error(e, ResponseData.WX_ACCESS_TOKEN_ERROR);
        }

        return wxMpService;
    }

    public WxMpConfigStorage wxMpConfigStorage(String wxCode) {
        WxMpInMemoryConfigStorage configStorage = new WxMpInMemoryConfigStorage();
        WxConfig wxConfig = getInstance(wxCode);
        configStorage.setAppId(wxConfig.getAppId());
        configStorage.setSecret(wxConfig.getAppSecret());
        configStorage.setToken(wxConfig.getEncodingToken());
        configStorage.setAesKey(wxConfig.getEncodingAesKey());
        return configStorage;
    }



}
