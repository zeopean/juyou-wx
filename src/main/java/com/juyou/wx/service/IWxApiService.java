package com.juyou.wx.service;

import com.juyou.wx.common.JsonResponse;
import com.juyou.wx.vos.QrcodeVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-06-26
 */
public interface IWxApiService {

    /**
     * 获取 accessToken
     *
     * @param wxCode
     * @return
     */
    JsonResponse getAccessToken(String wxCode, int isRefresh);

    /**
     * 发送客服消息
     *
     * @param wxCode
     * @param toUserOpenid
     * @param msgType
     * @param context
     * @return
     */
    JsonResponse sendKefuMessage(String wxCode, String toUserOpenid, String msgType, String context);

    /**
     * 创建菜单
     *
     * @param wxCode
     * @param menuJson
     * @return
     */
    JsonResponse createMenu(String wxCode, String menuJson);

    /**
     * 获取当前菜单
     *
     * @param wxCode
     * @return
     */
    JsonResponse getMenu(String wxCode);

    /**
     * 删除菜单
     *
     * @param wxCode
     * @return
     */
    JsonResponse delMenu(String wxCode);

    /**
     * 创建图片二维码
     *
     * @param wxCode
     * @param eventKey
     * @return
     */
    JsonResponse createQrcode(String wxCode, String eventKey);


    QrcodeVo getQrcodeVo(String wxCode, String eventKey);

    /**
     * url 图片上传至微信服务器
     *
     * @param wxCode
     * @param url
     * @return
     */
    JsonResponse uploadImgByUrl(String wxCode, String url);

    /**
     * file 文件上传
     *
     * @param wxCode
     * @param multipartFile
     * @return
     */
    JsonResponse uploadImgByFile(String wxCode, MultipartFile multipartFile);

    /**
     * 获取微信信息数组
     *
     * @return
     */
    JsonResponse getWxInfos();

    /**
     * 获取 单个 微信
     * @return
     */
    JsonResponse getWxOneInfo(String wxCode);

}
