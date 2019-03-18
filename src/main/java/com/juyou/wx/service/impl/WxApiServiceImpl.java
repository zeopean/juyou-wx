package com.juyou.wx.service.impl;

import com.juyou.wx.common.JsonResponse;
import com.juyou.wx.common.constants.Common;
import com.juyou.wx.common.constants.ResponseData;
import com.juyou.wx.common.constants.WxConstant;
import com.juyou.wx.config.wx.WechatType;
import com.juyou.wx.config.wx.WxInstance;
import com.juyou.wx.config.wx.bean.WxConfig;
import com.juyou.wx.service.IWxApiService;
import com.juyou.wx.util.DateTimeUtil;
import com.juyou.wx.util.FileUtil;
import com.juyou.wx.util.GeneralUtil;
import com.juyou.wx.util.ScannerUtil;
import com.juyou.wx.util.logger.LogUtil;
import com.juyou.wx.vos.QrcodeVo;
import com.juyou.wx.vos.TokenVo;
import com.juyou.wx.vos.WxInfoVo;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-06-26
 */
@Primary
@Service
public class WxApiServiceImpl implements IWxApiService {


    private final static String WX_BEAN_PACKAGE = "com.juyou.wx.config.wx.bean";

    @Resource
    private WxInstance wxInstance;


    @Override
    public JsonResponse getAccessToken(String wxCode, int isRefresh) {
        try {
            String token = null;
            if (Common.YES_INT == isRefresh) {
                token = wxInstance.wxMpService(wxCode).getAccessToken();

            } else {
                // 取出 instance 中的微信配置
                token = wxInstance.wxMpService(wxCode).getWxMpConfigStorage().getAccessToken();

            }

            TokenVo tokenVo = new TokenVo(wxCode, token, WxConstant.TOKEN_EXPIRED);
            return new JsonResponse().successResponse(tokenVo);

        } catch (WxErrorException ex) {

            LogUtil.error(ex, ResponseData.WX_ACCESS_TOKEN_ERROR.getCode(), "params: wxCode = %s", wxCode);
        }
        return new JsonResponse(ResponseData.REQUEST_ERROR);
    }


    @Override
    public JsonResponse sendKefuMessage(String wxCode, String toUserOpenid, String msgType, String content) {
        try {
            WxMpKefuMessage wxMpKefuMessage = kefuMessageBuilder(toUserOpenid, msgType, content);
            if (GeneralUtil.isNull(wxMpKefuMessage)) {
                return new JsonResponse(ResponseData.REQUEST_FAIL);
            }
            boolean res = wxInstance.wxMpService(wxCode).getKefuService().sendKefuMessage(wxMpKefuMessage);
            if (res) {
                return new JsonResponse().successResponse();
            }
            return new JsonResponse().failResponse(ResponseData.WX_SEND_KEFU_MSG_ERROR);
        } catch (WxErrorException ex) {
            // 日志记录
            LogUtil.error(ex, ResponseData.WX_SEND_KEFU_MSG_ERROR.getCode(), "params: wxCode = %s, toUserOpenid = %s, msgType = %s, content = %s", wxCode, toUserOpenid, msgType, content);

        }
        return new JsonResponse(ResponseData.REQUEST_FAIL);
    }

    /**
     * 客服消息构建器
     *
     * @param toUserOpenid
     * @param msgType
     * @param content
     * @return
     */
    private WxMpKefuMessage kefuMessageBuilder(String toUserOpenid, String msgType, String content) {


        if (WxConsts.KefuMsgType.TEXT.equals(msgType)) {
            return WxMpKefuMessage.TEXT().toUser(toUserOpenid).content(content).build();
        }

        if (WxConsts.KefuMsgType.IMAGE.equals(msgType)) {
            return WxMpKefuMessage.IMAGE().toUser(toUserOpenid).mediaId(content).build();
        }

        if (WxConsts.KefuMsgType.NEWS.equals(msgType)) {
            return WxMpKefuMessage.NEWS().articles(null).build();
        }

        return null;

    }


    @Override
    public JsonResponse createMenu(String wxCode, String menuJson) {
        try {

            String res = wxInstance.wxMpService(wxCode).getMenuService().menuCreate(menuJson);

            return new JsonResponse().successResponse();

        } catch (WxErrorException ex) {

            LogUtil.error(ex, ResponseData.WX_CREATE_MENU_ERROR.getCode(), "params: wxCode = %s, menuJson = %s", wxCode, menuJson);

        }
        return new JsonResponse().failResponse();
    }


    @Override
    public JsonResponse getMenu(String wxCode) {
        try {

            WxMpMenu wxMpMenu = wxInstance.wxMpService(wxCode).getMenuService().menuGet();

            return new JsonResponse().successResponse(wxMpMenu.getMenu());

        } catch (WxErrorException ex) {
            LogUtil.error(ex, ResponseData.WX_GET_MENU_ERROR.getCode(), "params: wxCode = %s", wxCode);

        }
        return new JsonResponse().failResponse();
    }

    @Override
    public JsonResponse delMenu(String wxCode) {
        try {

            wxInstance.wxMpService(wxCode).getMenuService().menuDelete();

            return new JsonResponse().successResponse();

        } catch (WxErrorException ex) {

            LogUtil.error(ex, ResponseData.WX_DELETE_MENU_ERROR.getCode(), "params: wxCode = %s", wxCode);
        }

        return new JsonResponse().failResponse();
    }


    @Override
    public JsonResponse createQrcode(String wxCode, String eventKey) {

        QrcodeVo qrcodeVo = getQrcodeVo(wxCode, eventKey);

        if (null == qrcodeVo) {
            return new JsonResponse().failResponse();
        }
        return new JsonResponse().successResponse(qrcodeVo);

    }

    @Override
    public QrcodeVo getQrcodeVo(String wxCode, String eventKey) {

        try {
            WxMpQrCodeTicket wxMpQrCodeTicket = wxInstance.wxMpService(wxCode).getQrcodeService().qrCodeCreateTmpTicket(eventKey, WxConstant.QR_EXPIRED_TIME);
            if (GeneralUtil.isNull(wxMpQrCodeTicket) || GeneralUtil.isObjNull(wxMpQrCodeTicket.getUrl())) {

                LogUtil.warn(ResponseData.WX_QRCODE_INVID_ERROR.getCode(), "params: wxCode = %s, eventKey = %s", wxCode, eventKey);

                return null;
            }
            String expireDatetime = DateTimeUtil.dayFormatDateToString(DateTimeUtil.nextMonth(new Date()));
            return new QrcodeVo(wxCode, eventKey, wxMpQrCodeTicket.getUrl(), expireDatetime);

        } catch (WxErrorException ex) {

            LogUtil.error(ex, ResponseData.WX_CREATE_QRCODE_ERROR.getCode(), "params: wxCode = %s, eventKey = %s", wxCode, eventKey);
        }

        return null;
    }

    @Override
    public JsonResponse uploadImgByUrl(String wxCode, String imgUrl) {
        try {
            // 判断文件bytes 是否存在
            if (GeneralUtil.isNull(imgUrl)) {
                return new JsonResponse().failResponse();
            }

            //得到输入流
            InputStream inputStream = FileUtil.getInputStreamByUrl(imgUrl);
            if (GeneralUtil.isNull(inputStream)) {
                return new JsonResponse().failResponse();
            }

            //获取自己数组
            byte[] bytes = FileUtil.readInputStream(inputStream);
            String filename = FileUtil.generateFilename() + "." + Common.IMAGE_JPG;
            File file = FileUtil.bytesCreateFile(bytes, filename);

            // 上传至微信服务器
            WxMediaUploadResult result = wxInstance.wxMpService(wxCode).getMaterialService().mediaUpload(WxConsts.MediaFileType.IMAGE, file);

            if (GeneralUtil.isNull(result)) {
                return new JsonResponse().failResponse();
            }

            // 删除文件
            file.delete();
            return new JsonResponse().successResponse(result);


        } catch (WxErrorException ex) {
            LogUtil.error(ex, ResponseData.WX_UPLOAD_ERROR.getCode(), "params: wxCode = %s, imgUrl = %s", wxCode, imgUrl);

        }

        return new JsonResponse().failResponse();
    }

    @Override
    public JsonResponse uploadImgByFile(String wxCode, MultipartFile multipartFile) {
        try {
            // 判断文件bytes 是否存在
            if (GeneralUtil.isNull(multipartFile) || Common.NO_INT == multipartFile.getBytes().length) {
                return new JsonResponse().failResponse();
            }
            String filename = FileUtil.generateFilename() + "." + Common.IMAGE_JPG;
            File file = FileUtil.bytesCreateFile(multipartFile.getBytes(), filename);

            // 上传至微信服务器
            WxMediaUploadResult result = wxInstance.wxMpService(wxCode).getMaterialService().mediaUpload(WxConsts.MediaFileType.IMAGE, file);

            if (GeneralUtil.isNull(result)) {
                return new JsonResponse().failResponse();
            }


            // 删除文件
            file.delete();
            return new JsonResponse().successResponse(result);

        } catch (IOException ex) {

            LogUtil.error(ex, ResponseData.WX_IO_UPLOAD_ERROR.getCode(), "params: wxCode = %s", wxCode);

        } catch (WxErrorException wxEx) {

            LogUtil.error(wxEx, ResponseData.WX_UPLOAD_ERROR.getCode(), "params: wxCode = %s", wxCode);
        }

        return new JsonResponse().failResponse();
    }

    @Override
    public JsonResponse getWxInfos() {
        // 获取微信 bean 数组
        List<Class<?>> classes = ScannerUtil.getClasses(WX_BEAN_PACKAGE);
        List<WxInfoVo> infoVoList = new ArrayList<>();
        for (Class<?> clazz : classes) {
            wxInfoHandle(infoVoList, clazz);
        }

        return new JsonResponse().successResponse(infoVoList);
    }

    private void wxInfoHandle(List<WxInfoVo> infoVoList, Class<?> clazz) {
        String wxCode = clazz.getSimpleName().toLowerCase();
        if (GeneralUtil.isNull(wxCode)) {
            return;
        }
        WxConfig wxConfig = wxInstance.getInstance(wxCode);
        if (GeneralUtil.isNull(wxConfig)) {
            return;
        }
        WxInfoVo wxInfoVo = new WxInfoVo(wxCode, WechatType.getWxNameByWxCode(wxCode), wxConfig.getNotifyUrl());
        infoVoList.add(wxInfoVo);
        wxInfoVo = null;
    }


    @Override
    public JsonResponse getWxOneInfo(String wxCode) {
        if (GeneralUtil.isNull(wxCode)) {
            return new JsonResponse().failResponse();
        }
        WxConfig wxConfig = wxInstance.getInstance(wxCode);
        if (GeneralUtil.isNull(wxConfig)) {
            LogUtil.warn(ResponseData.WX_CODE_ERROR.getCode(), "params: wxCode = %s", wxCode);
            return new JsonResponse().failResponse(ResponseData.WX_CODE_ERROR);
        }

        WxInfoVo wxInfoVo = new WxInfoVo(wxCode, WechatType.getWxNameByWxCode(wxCode), wxConfig.getNotifyUrl());

        return new JsonResponse().successResponse(wxInfoVo);
    }

}
