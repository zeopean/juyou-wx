package com.juyou.wx.service.impl;

import com.juyou.wx.common.JsonResponse;
import com.juyou.wx.common.constants.ResponseData;
import com.juyou.wx.config.wx.WxInstance;
import com.juyou.wx.service.IMenuService;
import com.juyou.wx.util.logger.LogUtil;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.stream.Collectors;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-09-27
 */
@Primary
@Service
public class MenuServiceImpl implements IMenuService {

    private static final String configDir = "src/main/resources/card.menu/card.%s.menu.json";

    @Autowired
    private WxInstance wxInstance;

    private String getMenuConfig(String wxCode) {
        if (null == wxCode) {
            return null;
        }
        try {
            InputStream inputStream = new FileSystemResource(String.format(configDir, wxCode)).getInputStream();
            return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining(System.lineSeparator()));

        } catch (FileNotFoundException e) {
            LogUtil.error(e, 0, "FileNotFoundException");

        } catch (IOException e1) {
            LogUtil.error(e1, 0, "IOException");

        }
        return null;
    }

    @Override
    public JsonResponse create(String wxCode) {
        String menuJson = getMenuConfig(wxCode);
        try {
            String res = wxInstance.wxMpService(wxCode).getMenuService().menuCreate(menuJson);

            return new JsonResponse().successResponse();

        } catch (WxErrorException ex) {

            LogUtil.error(ex, ResponseData.WX_CREATE_MENU_ERROR.getCode(), "params: wxCode = %s, menuJson = %s", wxCode, menuJson);

        }
        return new JsonResponse().failResponse();
    }
}
