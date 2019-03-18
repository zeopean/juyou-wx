package com.juyou.test;

import com.juyou.wx.AppApplication;
import com.juyou.wx.util.logger.LogUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApplication.class)
@EnableConfigurationProperties
public class AppApplicationTests {
//
//    @Resource
//    protected WxInstance wxInstance;
//
//    @Resource
//    protected IWxapiService wxapiService;
//
//


//    @Test
//    public void redisTest() {
//        redisService.set("name", "zeopean", 1000L);
//        String name = redisService.get("name", String.class);
//
//
//        WxConfig wxConfig = wxInstance.getInstance(WechatType.Yida.getWxCode());
//        redisService.set("wx", wxConfig, 1000L);
//
//        wxConfig = redisService.get("wx", WxConfig.class);
//
//    }

    @Test
    public void logTest() {

        LogUtil.info(100, "test1");
//
//        try {
//            System.out.print(1/0);
//        } catch (Exception e) {
//            LogUtil.error(e,100, "test2");
//
//            LogUtil.error(e,ResponseCode.REQUEST_FAIL);
//        }
    }







}
