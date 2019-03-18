package com.juyou.test;

import com.juyou.wx.common.JsonResponse;
import com.juyou.wx.config.wx.WechatType;
import com.juyou.wx.service.IMenuService;
import com.juyou.wx.service.IWxApiService;
import com.juyou.wx.util.logger.LogUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class WxServiceTests extends AppApplicationTests {

    @Autowired
    private IWxApiService wxBaseService;

    @Autowired
    private IMenuService menuService;

    @Test
    public void getToken() {
        JsonResponse json = wxBaseService.getAccessToken(WechatType.Default.getWxCode(), 0);

    }

    @Test
    public void menu()
    {
        menuService.create("default");
    }

    @Test
    public void log()
    {
        try {
            int i = 1/0;

        } catch (Exception e) {
            LogUtil.error(e, 0, "异常测试-邮件");
        }
    }
}