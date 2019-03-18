package com.juyou.wx.task;

import com.juyou.wx.common.constants.ResponseData;
import com.juyou.wx.config.wx.WxInstance;
import com.juyou.wx.util.logger.LogUtil;
import com.juyou.wx.vos.WxKefuMessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-09-02
 */
@Component
@EnableScheduling
public class WxKefuMessageTask {

    private static BlockingQueue<WxKefuMessageVo> queue = new ArrayBlockingQueue<>(10000);
    private static boolean running = false;

    @Autowired
    private WxInstance wxInstance;

    public static void addTask(WxKefuMessageVo wxKefuMessageVo) {
        if (null == wxKefuMessageVo) {
            return;
        }
        if (!queue.offer(wxKefuMessageVo)) {
            LogUtil.error(ResponseData.REQUEST_FAIL.getCode(), "WxKefuMessageTask queue is full!");
        }
    }

    public static void addTasks(List<WxKefuMessageVo> wxKefuMessageVos) {
        if (null == wxKefuMessageVos || wxKefuMessageVos.size() == 0) {
            return;
        }
        for (WxKefuMessageVo wxKefuMessageVo : wxKefuMessageVos) {
            addTask(wxKefuMessageVo);
        }
    }


    @Scheduled(cron = "* * * * * ?")
    public void send() {
        if (running) {
            return;
        }

        try {
            WxKefuMessageVo wxKefuMessageVo = null;
            wxKefuMessageVo = queue.poll();
            if (null == wxKefuMessageVo) {
                running = false;
                return;
            }

            wxInstance.wxMpService(wxKefuMessageVo.getWxCode()).getKefuService().sendKefuMessage(wxKefuMessageVo.getWxMpKefuMessage());
            running = false;

        } catch (Exception e1) {
            LogUtil.error(e1, ResponseData.REQUEST_FAIL.getCode(), "send invicard message error");
        }

    }

}
