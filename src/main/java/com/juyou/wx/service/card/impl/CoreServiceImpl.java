package com.juyou.wx.service.card.impl;

import com.juyou.wx.common.constants.CacheKey;
import com.juyou.wx.entity.dao.InvicardDao;
import com.juyou.wx.entity.dto.Invicard;
import com.juyou.wx.entity.dto.User;
import com.juyou.wx.service.IUserService;
import com.juyou.wx.service.card.*;
import com.juyou.wx.service.card.bean.*;
import com.juyou.wx.service.card.helper.CardHelper;
import com.juyou.wx.task.WxKefuMessageTask;
import com.juyou.wx.util.DateTimeUtil;
import com.juyou.wx.util.GeneralUtil;
import com.juyou.wx.util.JsonUtil;
import com.juyou.wx.util.cache.CacheExpiredUtil;
import com.juyou.wx.util.cache.RedisService;
import com.juyou.wx.util.logger.LogUtil;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created with idea
 * Description: 邀请卡服务
 *
 * @author zeopean
 * Date: 2018-08-06
 */
@Primary
@Service
public class CoreServiceImpl implements ICoreService {

    private static final String configPath = "src/main/resources/card.conf/card.%s.conf.json";
    private static final String eventMsgPath = "src/main/resources/card.conf/event.msg.json";

    private static CardConfig cardConfig;

    private static EventMsg eventMsg;

    @Autowired
    private IUserService userService;

    @Autowired
    private InvicardDao invicardDao;

    @Autowired
    private IKefuMsgService kefuMsgService;

    @Autowired
    private IImageService imageService;

    @Autowired
    private RedisService cache;

    @Autowired
    private IXmlOutMsgService xmlOutMsgService;

    @Autowired
    private IActivateService activateService;

    /**
     * 获取 邀请卡配置对象
     *
     * @param code
     * @return
     */
    private CardConfig getCardConfig(int code) {
        if (0 >= code) {
            return null;
        }
        try {
            if (null == cardConfig || code != cardConfig.getCode()) {
                InputStream inputStream = new FileSystemResource(String.format(configPath, code)).getInputStream();
                String configJson = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining(System.lineSeparator()));

                cardConfig = JsonUtil.parseObject(configJson, CardConfig.class);
            }
            return cardConfig;
        } catch (FileNotFoundException e) {
            LogUtil.error(e, 0, "FileNotFoundException");

        } catch (IOException e1) {
            LogUtil.error(e1, 0, "IOException");
        }
        return null;
    }

    /**
     * 获取事件消息对象
     *
     * @return
     */
    private EventMsg getEventMsg() {
        try {
            if (null == eventMsg) {
                InputStream inputStream = new FileSystemResource(eventMsgPath).getInputStream();
                String configJson = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining(System.lineSeparator()));

                eventMsg = JsonUtil.parseObject(configJson, EventMsg.class);
            }
            return eventMsg;
        } catch (FileNotFoundException e) {
            LogUtil.error(e, 0, "FileNotFoundException");

        } catch (IOException e1) {
            LogUtil.error(e1, 0, "IOException");
        }
        return null;
    }

    @Override
    public WxMpXmlOutMessage wxEventHandler(WxMpXmlMessage inMessage, String wxCode) {
        try {
            int code = CardHelper.getCardCodeBySceneStr(inMessage.getEventKey());
            CardConfig cardConfig = getCardConfig(code);
            if (null == cardConfig) {
                return null;
            }

            if (WxConsts.EventType.SUBSCRIBE.equals(inMessage.getEvent()) || WxConsts.EventType.SCAN.equals(inMessage.getEvent())) {
                subscribeAndScanMessageHandler(inMessage, cardConfig, wxCode);
                return null;
            }

            if (WxConsts.EventType.CLICK.equals(inMessage.getEvent())) {
                return clickEventMessageHandler(inMessage, cardConfig, wxCode);
            }

            if (WxConsts.EventType.UNSUBSCRIBE.equals(inMessage.getEvent())) {
                unsubscribeHandler(inMessage);
                return null;
            }

            defaultHandler(inMessage, cardConfig, wxCode);

        } catch (Exception e) {
            LogUtil.error(e, 0, "Exception");

        }
        return null;
    }

    /**
     * subscribe + scan 动作处理
     *
     * @param inMessage
     * @param cardConfig
     * @param wxCode
     */
    private void subscribeAndScanMessageHandler(WxMpXmlMessage inMessage, CardConfig cardConfig, String wxCode) {
        boolean refresh = false;
        if (WxConsts.EventType.SUBSCRIBE.equals(inMessage.getEvent())) {
            refresh = true;
        }
        User scanner = userService.eventUserHandler(inMessage, wxCode, refresh);
        if (null == scanner) {
            return;
        }
        User sharer = userService.getUserByEventKey(inMessage.getEventKey());
        String shareOpenid = "";
        if (null != sharer) {
            shareOpenid = sharer.getOpenid();
        }
        // 判断 是否为用户自己扫自己
        if (isSelf(scanner, shareOpenid)) {
            return;
        }

        // 添加 - 文本 - 客服消息
        if (cardConfig.getCanEventType().contains(inMessage.getEvent()) && GeneralUtil.isListNotNull(cardConfig.getScannerMsgs())) {
            // 判断是否 已经贡献过
            boolean hasScan = invicardDao.checkIsScan(cardConfig.getCode(), scanner.getId());
            String index = "0";
            if (hasScan) {
                sharer = null;
            }
            // index = 扫码人数
            scannerMsgsHandler(cardConfig, wxCode, scanner, sharer, index, inMessage.getEvent());
        }

        // 添加邀请卡图片 - 客服消息
        sendMessageForScanEvent(inMessage, cardConfig, wxCode);

        if (null == sharer) {
            return;
        }
        // 添加操作记录
        invicardDao.create(cardConfig.getCode(), scanner.getId(), sharer.getId());
    }

    private void scannerMsgsHandler(CardConfig cardConfig, String wxCode, User scanner, User sharer, String index, String eventType) {
        if (GeneralUtil.isListNull(cardConfig.getScannerMsgs())) {
            return;
        }
        for (ScannerMsg scannerMsg : cardConfig.getScannerMsgs()) {
            if (scannerMsg.getIndex().equals(index)) {
                kefuMsgService.kefuScannerMsgSend(cardConfig, scannerMsg, wxCode, scanner, sharer, eventType);
            }
        }
    }


    /**
     * 判定是否是自己扫自己
     *
     * @param scanner
     * @param openid
     * @return
     */
    private boolean isSelf(User scanner, String openid) {
        if (null == scanner || null == openid) {
            return false;
        }

        if (scanner.getOpenid().equals(openid)) {
            return true;
        }
        return false;
    }


    /**
     * 为扫码 的 用户发送消息
     *
     * @param inMessage
     * @param cardConfig
     * @param wxCode
     */
    private void sendMessageForScanEvent(WxMpXmlMessage inMessage, CardConfig cardConfig, String wxCode) {

        // 扫码人 动作
        sendMessageForOperator(inMessage, cardConfig, wxCode);

        // 分享二维码的人
        User sharer = userService.getUserByEventKey(inMessage.getEventKey());
        if (null == sharer) {
            return;
        }
        // 分享者的判断
        boolean sharerIsReceive = invicardDao.checkIsReceive(cardConfig.getCode(), sharer.getId());
        if (!sharerIsReceive && GeneralUtil.isListNotNull(cardConfig.getSharerMsgs())) {
            User scanner = userService.findOneByOpenid(inMessage.getFromUser());

            if (null != scanner) {
                // 通知 分享人 +1 表示加上当前扫码用户
                int scanNum = invicardDao.countScanNum(cardConfig.getCode(), sharer.getId()) + 1;

                sharerMsgsHandler(cardConfig, wxCode, scanner, sharer, scanNum);

                // 解锁成功操作
                if (scanNum >= cardConfig.getInviteNum()) {
                    successSharerReciveHandler(cardConfig, sharer, wxCode);
                }
            }
        }
        // 更新邀请卡数据记录
        activateService.updateInvicardLog(inMessage, null, cardConfig);
    }

    private void sharerMsgsHandler(CardConfig cardConfig, String wxCode, User scanner, User sharer, int scanNum) {
        if (GeneralUtil.isListNull(cardConfig.getScannerMsgs())) {
            return;
        }
        String cacheKey = "";
        for (SharerMsg sharerMsg : cardConfig.getSharerMsgs()) {
            if (!sharerMsg.getIndex().equals(scanNum + "")) {
                continue;
            }

            cacheKey = String.format(CacheKey.CACHE_KEY_MSG_LOCK, cardConfig.getCode(), sharerMsg.getName());
            if (cache.exists(cacheKey)) {
                return;
            }
            kefuMsgService.kefuSharerMsgSend(cardConfig, sharerMsg, wxCode, scanner, sharer, cardConfig.getInviteNum() - scanNum);
            // 只发送一次的文本消息
            if (1 == sharerMsg.getOnlyOnce()) {
                cache.set(cacheKey, 1, CacheExpiredUtil.getDay(7));
            }
        }

    }

    /**
     * 为扫码操作者 发送消息
     *
     * @param inMessage
     * @param cardConfig
     * @param wxCode
     */
    private void sendMessageForOperator(WxMpXmlMessage inMessage, CardConfig cardConfig, String wxCode) {
        String formUserOpenid = inMessage.getFromUser();
        // 扫码人
        User operator = userService.findOneByOpenid(inMessage.getFromUser());
        if (null == operator) {
            return;
        }

        // 判断扫码者是否解锁
        boolean scannerIsReceive = invicardDao.checkIsReceive(cardConfig.getCode(), operator.getId());

        // 判断扫码者的判断
        if (scannerIsReceive) {
            // 发送 解锁成功 的消息
            kefuMsgService.receiveSuccessMsgConstruct(wxCode, formUserOpenid, cardConfig);

        } else {

            String cacheKey = String.format(CacheKey.INVICARD_IMAGE_MEDIA_ID, wxCode, inMessage.getFromUser());
            // 发送邀请卡图片
            String mediaId = cache.get(cacheKey, String.class);
            if (GeneralUtil.isObjNull(mediaId)) {
                mediaId = imageService.getMediaId(inMessage, wxCode, operator, cardConfig);
            }
            WxKefuMessageTask.addTask(kefuMsgService.imgMsgConstruct(wxCode, formUserOpenid, mediaId));

            // 添加邀请卡如日志
            activateService.createInvicardLog(inMessage, operator, mediaId, cardConfig);
        }
    }


    /**
     * 取消关注 处理
     *
     * @param inMessage
     */
    private void unsubscribeHandler(WxMpXmlMessage inMessage) {
        User unsubscriber = userService.findOneByOpenid(inMessage.getFromUser());
        userService.unsubscribe(unsubscriber);
    }

    /**
     * 点击菜单 事件 消息处理
     *
     * @param inMessage
     * @param cardConfig
     * @param wxCode
     */
    private WxMpXmlOutMessage clickEventMessageHandler(WxMpXmlMessage inMessage, CardConfig cardConfig, String wxCode) {
        if (GeneralUtil.isMultiHasNull(inMessage, cardConfig, wxCode)) {
            return null;
        }
        User scanner = userService.findOneByOpenid(inMessage.getFromUser());
        if (null == scanner) {
            scanner = userService.eventUserHandler(inMessage, wxCode, false);
        }
        if (null == scanner) {
            return null;
        }
        String eventKey = inMessage.getEventKey();
        if (GeneralUtil.isObjNotNull(eventKey) && !eventKey.contains(CardHelper.EVENT_KEY_SCENE_STR_PREFIX)) {
            return menuClickEventMessageHandler(inMessage, wxCode);
        }

        sendMessageForOperator(inMessage, cardConfig, wxCode);
        return menuClickEventMessageHandler(inMessage, wxCode);
    }

    private void successSharerReciveHandler(CardConfig cardConfig, User sharer, String wxCode) {
        // 更新邀请卡状态
        Invicard invicard = invicardDao.findOneForScanner(cardConfig.getCode(), sharer.getId());
        if (null == invicard) {
            return;
        }
        invicard.setIsReceive(1);
        invicard.setUpdateTime(new Date());
        invicardDao.updateById(invicard);

        // 发送消息
        kefuMsgService.receiveSuccessMsgConstruct(wxCode, sharer.getOpenid(), cardConfig);
    }

    /**
     * 处理菜单事件消息
     *
     * @param inMessage
     * @param wxCode
     */
    private WxMpXmlOutMessage menuClickEventMessageHandler(WxMpXmlMessage inMessage, String wxCode) {

        EventMsg eventMsg = getEventMsg();
        if (null == eventMsg || GeneralUtil.isListNull(eventMsg.getMessages())) {
            return null;
        }
        if (!eventMsg.getWxCodes().contains(wxCode)) {
            return null;
        }
        String eventKey = inMessage.getEventKey();

        WxMpXmlOutNewsMessage.Item item = null;
        WxMpXmlOutNewsMessage wxMpXmlOutNewsMessage = new WxMpXmlOutNewsMessage();
        for (Msg msg : eventMsg.getMessages()) {

            if (!eventKey.equals(msg.getName())) {
                continue;
            }
            if (WxConsts.KefuMsgType.TEXT.equals(msg.getType())) {
                return xmlOutMsgService.textMsgConstruct(msg, inMessage);
            }

            if (WxConsts.KefuMsgType.MINIPROGRAMPAGE.equals(msg.getType())) {
                WxKefuMessageTask.addTask(kefuMsgService.miniappMsgConstruct(wxCode, inMessage.getFromUser(), msg.getTitle(), msg.getAppid(), msg.getPagePath(), msg.getImageUrl()));
            }

            if (WxConsts.KefuMsgType.NEWS.equals(msg.getType())) {
                item = new WxMpXmlOutNewsMessage.Item();

                item.setDescription(msg.getDescription());
                item.setTitle(msg.getTitle());
                item.setUrl(msg.getUrl());
                item.setPicUrl(msg.getImageUrl());
                wxMpXmlOutNewsMessage.setToUserName(inMessage.getFromUser());
                wxMpXmlOutNewsMessage.setFromUserName(inMessage.getToUser());
                wxMpXmlOutNewsMessage.setCreateTime(DateTimeUtil.dayFormatDateToLong(new Date()));
                wxMpXmlOutNewsMessage.addArticle(item);

            }
        }

        if (wxMpXmlOutNewsMessage.getArticleCount() > 0) {
            return wxMpXmlOutNewsMessage;
        }

        return null;
    }

    /**
     * 默认 动作
     *
     * @param inMessage
     * @param cardConfig
     * @param wxCode
     */
    private void defaultHandler(WxMpXmlMessage inMessage, CardConfig cardConfig, String wxCode) {
        if (null == cardConfig) {
            return;
        }

        // 添加 - 文本 - 客服消息
        if (cardConfig.getCanEventType().contains(inMessage.getEvent()) && GeneralUtil.isListNotNull(cardConfig.getScannerMsgs())) {
            User scanner = userService.findOneByOpenid(inMessage.getFromUser());
            // index = 扫码人数
            scannerMsgsHandler(cardConfig, wxCode, scanner, null, "0", inMessage.getEvent());
            // 添加邀请卡图片 - 客服消息
            sendMessageForScanEvent(inMessage, cardConfig, wxCode);
        }
        // 更新邀请卡数据记录
        activateService.updateInvicardLog(inMessage, null, cardConfig);
    }

    /**
     * 更新邀请卡数据
     *
     * @param inMessage
     * @param wxCode
     */
    private void updateInvicardLog(WxMpXmlMessage inMessage, String wxCode) {

    }

    @Override
    public WxMpXmlOutMessage wxTextHandler(WxMpXmlMessage inMessage, String wxCode) {

        return null;
    }
}
