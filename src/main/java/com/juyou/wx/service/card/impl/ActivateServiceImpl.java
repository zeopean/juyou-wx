package com.juyou.wx.service.card.impl;

import com.juyou.wx.entity.dao.InvicardLogDao;
import com.juyou.wx.entity.dto.InvicardLog;
import com.juyou.wx.entity.dto.User;
import com.juyou.wx.service.card.IActivateService;
import com.juyou.wx.service.card.bean.CardConfig;
import com.juyou.wx.service.card.helper.CardHelper;
import com.juyou.wx.task.CardScanNumTask;
import com.juyou.wx.util.GeneralUtil;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-10-09
 */
@Primary
@Service
public class ActivateServiceImpl implements IActivateService {

    @Autowired
    private InvicardLogDao invicardLogDao;

    @Override
    public boolean createInvicardLog(WxMpXmlMessage inMessage, User scanner, String mediaId, CardConfig cardConfig) {
        if (null == scanner) {
            return false;
        }
        InvicardLog inviCardLog = new InvicardLog();
        inviCardLog.setCode(cardConfig.getCode());
        int isActive = 1;
        if (CardHelper.getUserIdBySceneStr(inMessage.getEventKey()) <= 0) {
            isActive = 0;
        }

        inviCardLog.setIsActiveInto(isActive);

        int isNewUser = 0;
        if (null == scanner.getCreateTime() && scanner.getCreateTime().equals(scanner.getUpdateTime())) {
            isNewUser = 1;
        }
        inviCardLog.setIsNewUser(isNewUser);
        inviCardLog.setMediaId(mediaId);
        inviCardLog.setCreateTime(new Date());
        inviCardLog.setUpdateTime(new Date());
        inviCardLog.setOpenid(scanner.getOpenid());
        inviCardLog.setUnionid(scanner.getUnionid());
        return invicardLogDao.insert(inviCardLog);
    }

    @Override
    public void updateInvicardLog(WxMpXmlMessage inMessage, User sharer, CardConfig cardConfig) {
        if (GeneralUtil.isObjNull(inMessage.getEventKey())) {
            return;
        }
        if (GeneralUtil.isNull(sharer)) {
            return;
        }
        InvicardLog inviCardLog = invicardLogDao.findOne(sharer.getId(), cardConfig.getCode() + "");
        if (GeneralUtil.isNull(inviCardLog)) {
            return;
        }
        // 进行更新操作
        CardScanNumTask.addTask(inviCardLog.getId());
    }
}
