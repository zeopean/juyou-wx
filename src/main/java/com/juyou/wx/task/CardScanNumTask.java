package com.juyou.wx.task;

import com.juyou.wx.common.constants.ResponseData;
import com.juyou.wx.entity.dao.InvicardLogDao;
import com.juyou.wx.util.GeneralUtil;
import com.juyou.wx.util.cache.CacheExpiredUtil;
import com.juyou.wx.util.cache.RedisService;
import com.juyou.wx.util.logger.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author zeopean
 * 更新邀请卡 扫码人数
 */
@Component
@EnableScheduling
public class CardScanNumTask {


    private static BlockingQueue<Long> queue = new ArrayBlockingQueue<>(10000);

    private static String key = "juyou.increase.share.num";

    @Autowired
    private InvicardLogDao invicardLogDao;

    @Autowired
    private RedisService redisService;


    public static void addTask(long logId) {
        if (!queue.offer(logId)) {
            LogUtil.error(ResponseData.WX_CODE_ERROR.getCode(), "CardShareNumJob queue is full!");
        }
    }

    @Scheduled(cron = "* * * * * ?")
    public void doJob() {
        String cache = redisService.get(key, String.class);
        if (GeneralUtil.isObjNotNull(cache)) {
            return;
        }
        redisService.set(key, "1", CacheExpiredUtil.getSecond(3));

        List<Long> logIds = new ArrayList<>();
        queue.drainTo(logIds);

        if (GeneralUtil.isListNotNull(logIds)) {
            Map<Long, Integer> idMap = new HashMap<>();
            for (Long id : logIds) {
                if (idMap.containsKey(id)) {
                    idMap.put(id, idMap.get(id) + 1);
                } else {
                    idMap.put(id, 1);
                }
            }

            // 进行遍历, 更新课程参加人数数据
            if (GeneralUtil.isMapNull(idMap)) {
                return;
            }

            Iterator<Map.Entry<Long, Integer>> iterator = idMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Long, Integer> entry = iterator.next();
                if (GeneralUtil.isNull(entry)) {
                    continue;
                }

                if (entry.getValue() > 0) {
                    invicardLogDao.increment(entry.getKey(), entry.getValue());
                }
            }

        }
    }

}


