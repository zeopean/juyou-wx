package com.juyou.wx.service.card.helper;

import com.juyou.wx.util.GeneralUtil;

/**
 * @author zeopean
 * 微信 helper 类
 */
public final class CardHelper {


    private static final String WX_EVENT_KEY_SCENE = "qrscene_";

    public static final String EVENT_KEY_SCENE_STR_PREFIX = "jy_";

    private static final String SCENE_STR_FORMAT = "jy_%s_%s";


    /**
     * 通过场景 SceneStr 获取 邀请卡活动code
     *
     * @param eventKey
     * @return
     */
    public static int getCardCodeBySceneStr(String eventKey) {
        if (null == eventKey || !eventKey.contains(EVENT_KEY_SCENE_STR_PREFIX)) {
            return 1;
        }

        // 微信传回来的 的 sub - eventKey 处理，有就直接过滤 qrscene_
        eventKey = eventKey.replace(WX_EVENT_KEY_SCENE, "");
        String[] args = eventKey.split("_");
        String cardCode = args[1];
        if (GeneralUtil.isNumeric(cardCode)) {
            return Integer.valueOf(cardCode);
        }
        return 1;
    }

    /**
     * 通过场景 id 找到 userId
     *
     * @param eventKey
     * @return
     */
    public static long getUserIdBySceneStr(String eventKey) {
        if (GeneralUtil.isObjNull(eventKey)) {
            return 0;
        }
        if (!eventKey.contains(EVENT_KEY_SCENE_STR_PREFIX)) {
            return 0;
        }

        // 微信传回来的 的 sub - eventKey 处理，有就直接过滤 qrscene_
        eventKey = eventKey.replace(WX_EVENT_KEY_SCENE, "");
        String[] args = eventKey.split("_");
        if (2 == args.length) {
            return 0;
        }
        String cardCode = args[2];

        if (GeneralUtil.isNumeric(cardCode)) {
            return Long.valueOf(cardCode);
        }
        return 0;
    }

    /**
     * 获取 sceneStr
     *
     * @param cardCode
     * @param userId
     * @return
     */
    public static String getUserSceneStr(int cardCode, long userId) {
        return String.format(SCENE_STR_FORMAT, cardCode, userId);
    }


}
