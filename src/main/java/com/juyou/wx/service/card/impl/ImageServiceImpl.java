package com.juyou.wx.service.card.impl;

import com.juyou.wx.common.constants.Common;
import com.juyou.wx.common.constants.ResponseData;
import com.juyou.wx.config.wx.WxInstance;
import com.juyou.wx.entity.dto.User;
import com.juyou.wx.service.IWxApiService;
import com.juyou.wx.service.card.IImageService;
import com.juyou.wx.service.card.bean.CardConfig;
import com.juyou.wx.service.card.bean.Picture;
import com.juyou.wx.service.card.consts.PictureType;
import com.juyou.wx.service.card.helper.CardHelper;
import com.juyou.wx.service.card.helper.ImageHelper;
import com.juyou.wx.util.GeneralUtil;
import com.juyou.wx.util.QrcodeUtil;
import com.juyou.wx.util.StringUtil;
import com.juyou.wx.util.logger.LogUtil;
import com.juyou.wx.vos.QrcodeVo;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * 图片工具类
 *
 * @author zeopean
 */
@Primary
@Service
public class ImageServiceImpl implements IImageService {

    private final static String TMP_DIR = "/tmp/card/";
    @Autowired
    private WxInstance wxInstance;
    @Autowired
    private IWxApiService wxApiService;

    @Override
    public String getMediaId(WxMpXmlMessage inMessage, String wxCode, User scanner, CardConfig cardConfig) {
        try {
            int code = CardHelper.getCardCodeBySceneStr(inMessage.getEventKey());

            // 用户昵称处理
            nicknameContentHandle(cardConfig, scanner);
            // 用户头像处理
            avatarImgUrlHandle(cardConfig, scanner);

            // 生成分享二维码
            String sceneStr = CardHelper.getUserSceneStr(code, scanner.getId());
            QrcodeVo qrcodeVo = wxApiService.getQrcodeVo(wxCode, sceneStr);
            String qrcode = null;
            if (null != qrcodeVo) {
                qrcode = qrcodeVo.getQrcodeUrl();
            }

            File file = pictureSynthesis(cardConfig.getPictures(), scanner, qrcode);

            // 上传至微信服务器
            WxMediaUploadResult result = wxInstance.wxMpService(wxCode).getMaterialService().mediaUpload(WxConsts.MediaFileType.IMAGE, file);
            file.delete();
            if (null != result) {
                return result.getMediaId();
            }

        } catch (WxErrorException e) {
            LogUtil.error(e, 0, "WxErrorException");

        }

        return null;
    }

    /**
     * 昵称
     *
     * @param cardConfig
     * @param user
     */
    private void nicknameContentHandle(CardConfig cardConfig, User user)
    {
        if (null == cardConfig || GeneralUtil.isListNull(cardConfig.getPictures())) {
            return;
        }
        for (Picture picture : cardConfig.getPictures()) {
            if(PictureType.nickname.equals(picture.getType())) {
                // 过滤表情
                picture.getPicFont().setContent(StringUtil.filterOffUtf8Mb4(user.getNickname()) + picture.getPicFont().getDefaultContent());
            }
        }
    }

    /**
     * 头像
     *
     * @param cardConfig
     * @param user
     */
    private void avatarImgUrlHandle(CardConfig cardConfig, User user)
    {
        if (null == cardConfig || GeneralUtil.isListNull(cardConfig.getPictures())) {
            return;
        }
        for (Picture picture : cardConfig.getPictures()) {
            if(PictureType.avatar.equals(picture.getType())) {
                picture.setUrlPath(user.getAvatar());
            }
        }
    }

    /**
     * 通过url 创建二维码图片
     *
     * @param qrcodeUrl
     * @return
     */
    private static BufferedImage createQrcodeImageByUrl(String qrcodeUrl, int width) {
        BufferedImage image = null;
        try {
            image = QrcodeUtil.createQRCodeImage(qrcodeUrl, width);

        } catch (Exception e) {
            e.printStackTrace();
            //重新发起
        }
        return image;
    }

    /**
     * 图片合成方法
     *
     * @param pictures
     * @return
     */
    public File pictureSynthesis(List<Picture> pictures, User scanner, String qrcode) {
        long start = System.currentTimeMillis();
        if (GeneralUtil.isListNull(pictures)) {
            return null;
        }

        try {
            //底图的二进制流
            String backgroundPath = getBackgroundPath(pictures);
            if (GeneralUtil.isObjNull(backgroundPath)) {
                return null;
            }
            // 对底图进行处理
            BufferedImage background = ImageIO.read(new FileSystemResource(backgroundPath).getInputStream());
            BufferedImage image = null;
            // 进行图片合成
            for (Picture picture : pictures) {
                if (1 == picture.getIsBackground()) {
                    continue;
                }
                if (1 == picture.getIsFont()) {
                    // 字体图片
                    ImageHelper.textMuilLineImage(background, picture);

                } else {
                    // 普通图片
                    if ("qrcode".equals(picture.getType())) {
                        picture.setUrlPath(qrcode);
                    }
                    image = pictureHandle(picture, scanner);
                    background = ImageHelper.overlapImage(background, image, picture.getStartX(), picture.getStartY());
                    image = null;
                }
            }

            if (null == background) {
                return null;
            }

            File dir = new File(TMP_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }

            String fileName = TMP_DIR + System.currentTimeMillis() + String.valueOf(Math.random()) + "." + Common.IMAGE_JPG;
            File file = new File(fileName);
            // 获取合成处理后的图片
            if (!ImageIO.write(background, Common.IMAGE_JPG, file)) {
                throw new IOException("card could not write an image of format jpg to " + file);
            }

            long end = System.currentTimeMillis();
            // 统计图片合成时间
            LogUtil.info("picture synthesis use time is {%s}", end - start);
            return file;

        } catch (Exception e) {
            LogUtil.error(e, ResponseData.SERVER_INNER_ERROR);

        }
        return null;
    }

    /**
     * Picture to BufferedImage
     *
     * @param picture
     * @return
     */
    private  BufferedImage pictureHandle(Picture picture, User scanner) {

        try {
            BufferedImage image = null;
            // 处理头像
            if (PictureType.avatar.equals(picture.getType())) {
                picture.setUrlPath(scanner.getAvatar());
            }
            // 处理分享二维码
            if (PictureType.qrcode.equals(picture.getType()) && GeneralUtil.isObjNotNull(picture.getUrlPath())) {
                picture.setUrlPath(picture.getUrlPath());
            }

            if (GeneralUtil.isObjNotNull(picture.getFilePath())) {
                image = ImageIO.read(new File(picture.getFilePath()));

            } else {
                if (GeneralUtil.isObjNotNull(picture.getUrlPath())) {
                    if (PictureType.qrcode.equals(picture.getType())) {
                        image = createQrcodeImageByUrl(picture.getUrlPath(), picture.getWidth());
                    } else {
                        image = ImageIO.read(new URL(picture.getUrlPath()));
                    }
                }
            }

            if (1 == picture.getIsCircle()) {
                return ImageHelper.convertCircular(picture, image);
            }

            return image;

        } catch (Exception e) {
            LogUtil.error(e, 0, "Exception");
        }

        return null;
    }


    /**
     * 获取背景图片
     *
     * @param pictures
     * @return
     */
    private String getBackgroundPath(List<Picture> pictures) {
        if (GeneralUtil.isListNull(pictures)) {
            return null;
        }
        for (Picture picture : pictures) {
            if (1 == picture.getIsBackground()) {
                return "src/main/resources/card/image/" + picture.getFilePath();
            }
        }
        return null;
    }


}
