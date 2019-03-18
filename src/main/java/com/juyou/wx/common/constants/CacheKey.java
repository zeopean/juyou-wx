package com.juyou.wx.common.constants;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-06-29
 */
public interface CacheKey {


    String WXAPI_ACCESS_TOKEN_APPID = "wxapi.access_token.%s";

    String WX_USER_ONE_OPENID = "wx.invicard.user.openid.%s";
    String WX_USER_ONE_ID = "wx.invicard.user.id.%s";

    String INVICARD_IMAGE_MEDIA_ID = "wx.invicard.image.mediaId.%s.%s";

    String INVI_TEXT_LOCK = "invicard.text.lock.%s.%s.%s";

    String CACHE_KEY_PICTURE_MEDIAID = "invicard.minipro.mediaid.%s";

    String CACHE_KEY_MSG_LOCK = "invicard.minipro.onlyonce.msg.lock.%s.%s";


}
