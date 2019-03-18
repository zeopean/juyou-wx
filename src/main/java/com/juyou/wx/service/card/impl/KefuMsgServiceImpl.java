package com.juyou.wx.service.card.impl;

import com.juyou.wx.common.constants.CacheKey;
import com.juyou.wx.config.wx.WxInstance;
import com.juyou.wx.entity.dao.InvicardDao;
import com.juyou.wx.entity.dto.User;
import com.juyou.wx.service.card.IKefuMsgService;
import com.juyou.wx.service.card.bean.CardConfig;
import com.juyou.wx.service.card.bean.Msg;
import com.juyou.wx.service.card.bean.Success;
import com.juyou.wx.task.WxKefuMessageTask;
import com.juyou.wx.util.GeneralUtil;
import com.juyou.wx.util.MD5Util;
import com.juyou.wx.util.cache.CacheExpiredUtil;
import com.juyou.wx.util.cache.RedisService;
import com.juyou.wx.vos.WxKefuMessageVo;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-09-02
 */
@Primary
@Service
public class KefuMsgServiceImpl implements IKefuMsgService {

    @Autowired
    private InvicardDao invicardDao;

    @Autowired
    private RedisService redisService;

    @Autowired
    private WxInstance wxInstance;

    @Override
    public WxKefuMessageVo textMsgConstruct(String wxCode, String openid, String context) {
        WxMpKefuMessage wxMpKefuMessage = new WxMpKefuMessage();
        wxMpKefuMessage.setMsgType(WxConsts.KefuMsgType.TEXT);
        wxMpKefuMessage.setToUser(openid);
        wxMpKefuMessage.setContent(context);

        return new WxKefuMessageVo(wxMpKefuMessage, wxCode);
    }

    @Override
    public WxKefuMessageVo imgMsgConstruct(String wxCode, String openid, String mediaId) {
        WxMpKefuMessage wxMpKefuMessage = new WxMpKefuMessage();
        wxMpKefuMessage.setMsgType(WxConsts.KefuMsgType.IMAGE);
        wxMpKefuMessage.setToUser(openid);
        wxMpKefuMessage.setMediaId(mediaId);

        return new WxKefuMessageVo(wxMpKefuMessage, wxCode);
    }

    @Override
    public WxKefuMessageVo newsMsgConstruct(String wxCode, String openid, List<WxMpKefuMessage.WxArticle> wxArticles) {
        WxMpKefuMessage wxMpKefuMessage = new WxMpKefuMessage();
        wxMpKefuMessage.setMsgType(WxConsts.KefuMsgType.NEWS);
        wxMpKefuMessage.setToUser(openid);
        wxMpKefuMessage.setArticles(wxArticles);

        return new WxKefuMessageVo(wxMpKefuMessage, wxCode);

    }

    @Override
    public WxKefuMessageVo miniappMsgConstruct(String wxCode, String openid, String title, String appid, String pagePath, String imageUrl) {
        WxMpKefuMessage wxMpKefuMessage = new WxMpKefuMessage();
        wxMpKefuMessage.setMsgType(WxConsts.KefuMsgType.MINIPROGRAMPAGE);
        wxMpKefuMessage.setToUser(openid);
        wxMpKefuMessage.setTitle(title);
        wxMpKefuMessage.setMiniProgramAppId(appid);
        wxMpKefuMessage.setMiniProgramPagePath(pagePath);
        String mediaId = createThumbMediaId(imageUrl, wxCode);
        if (GeneralUtil.isObjNull(mediaId)) {
            return null;
        }
        wxMpKefuMessage.setThumbMediaId(mediaId);

        return new WxKefuMessageVo(wxMpKefuMessage, wxCode);
    }

    public String createThumbMediaId(String url, String wxCode) {
        String cacheKey = MD5Util.encodeByMD5(String.format(CacheKey.CACHE_KEY_PICTURE_MEDIAID, url));
        String mediaId = redisService.get(cacheKey, String.class);
        if (GeneralUtil.isObjNull(mediaId)) {
            try {
                BufferedImage image = ImageIO.read(new URL(url));
                String tmpDir = "/tmp/card/";
                File dir = new File(tmpDir);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                String fileName = tmpDir + UUID.randomUUID().toString() + ".png";
                File file = new File(fileName);
                // 获取处理后的图片
                if (!ImageIO.write(image, "png", file)) {
                    throw new IOException("Could not write an image of format png to " + file);
                }

                // 实现文件上传
                // 上传至微信服务器
                WxMediaUploadResult result = wxInstance.wxMpService(wxCode).getMaterialService().mediaUpload(WxConsts.MediaFileType.IMAGE, file);
                file.delete();
                if (null != result) {
                    return result.getMediaId();
                }

            } catch (Exception e) {

            }
        }

        return mediaId;
    }

    @Override
    public void receiveSuccessMsgConstruct(String wxCode, String openid, CardConfig cardConfig) {
        if (GeneralUtil.isListNull(cardConfig.getSuccess())) {
            return;
        }
        List<WxKefuMessageVo> wxKefuMessageVos = new ArrayList<>();
        for (Success success : cardConfig.getSuccess()) {
            if (null == success) {
                continue;
            }
            if (WxConsts.KefuMsgType.TEXT.equals(success.getType())) {
                wxKefuMessageVos.add(textMsgConstruct(wxCode, openid, success.getContent()));
            }

            if (WxConsts.KefuMsgType.NEWS.equals(success.getType())) {
                wxKefuMessageVos.add(newsMsgConstruct(wxCode, openid, success.getWxArticles()));
            }

            if (WxConsts.KefuMsgType.MINIPROGRAMPAGE.equals(success.getType())) {
                wxKefuMessageVos.add(newsMsgConstruct(wxCode, openid, success.getWxArticles()));
            }
        }
        WxKefuMessageTask.addTasks(wxKefuMessageVos);
    }


    @Override
    public void kefuScannerMsgSend(CardConfig cardConfig, Msg scannerMsg, String wxCode, User scanner, User sharer, String eventType) {
        if (null == scanner) {
            return;
        }
        String lock = String.format(CacheKey.INVI_TEXT_LOCK, cardConfig.getCode(), scanner.getId(), scannerMsg.getName());
        String scanNum = "" + invicardDao.countScanNum(cardConfig.getCode(), scanner.getId());
        if (WxConsts.KefuMsgType.TEXT.equals(scannerMsg.getType())) {
            String content = "";

            // 统计扫码人数
            if (1 == scannerMsg.getIsDefault()) {
                if (redisService.exists(lock)) {
                    return;
                }
                content = scannerMsg.getContent().replace(":scanner", scanner.getNickname()).replace(":scanNum", scanNum);
            } else {
                if (redisService.exists(lock)) {
                    return;
                }
                if (null == sharer) {
                    return;
                } else {
                    String sharerNickname = sharer.getNickname();
                    content = scannerMsg.getContent().replace(":scanner", scanner.getNickname()).replace(":sharer", sharerNickname).replace(":scanNum", scanNum).replace(":inviteNum", cardConfig.getInviteNum() + "");
                }
            }
            redisService.set(lock, 1, CacheExpiredUtil.getSecond(3));

            WxKefuMessageTask.addTask(textMsgConstruct(wxCode, scanner.getOpenid(), content));
            return;
        }

        if (WxConsts.KefuMsgType.IMAGE.equals(scannerMsg.getType())) {
            WxKefuMessageTask.addTask(imgMsgConstruct(wxCode, scanner.getOpenid(), scannerMsg.getMediaId()));
            return;
        }

        if (WxConsts.KefuMsgType.MINIPROGRAMPAGE.equals(scannerMsg.getType())) {
            WxKefuMessageTask.addTask(miniappMsgConstruct(wxCode, scanner.getOpenid(), scannerMsg.getTitle(), scannerMsg.getAppid(), scannerMsg.getPagePath(), scannerMsg.getImageUrl()));
            return;
        }

    }

    @Override
    public void kefuSharerMsgSend(CardConfig cardConfig, Msg sharerMsg, String wxCode, User scanner, User sharer, int needNum) {
        if (null == scanner || null == sharer) {
            return;
        }
        if (WxConsts.KefuMsgType.TEXT.equals(sharerMsg.getType())) {
            String scannerNickname = scanner.getNickname();
            String content = sharerMsg.getContent().replace(":sharer", scannerNickname).replace(":needNum", needNum + "");
            WxKefuMessageTask.addTask(textMsgConstruct(wxCode, sharer.getOpenid(), content));
            return;
        }
    }
}
