package com.juyou.wx.entity.dao.impl;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.juyou.wx.common.constants.Columns;
import com.juyou.wx.entity.dao.InvicardLogDao;
import com.juyou.wx.entity.dto.InvicardLog;
import com.juyou.wx.entity.mapper.InvicardLogMapper;
import com.juyou.wx.util.GeneralUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 *
 * @author zeopean
 * @since 2018-09-20
 */
@Primary
@Service
public class InvicardDaoLogImpl extends ServiceImpl<InvicardLogMapper, InvicardLog> implements InvicardLogDao {

    @Override
    public InvicardLog findOne(Long userId, String code) {
        return super.selectOne(new Condition().eq(Columns.userId, userId).eq(Columns.code, code));
    }

    @Override
    public boolean increment(Long id, int num) {
        InvicardLog invicardLog = super.selectById(id);
        if (GeneralUtil.isNull(invicardLog)) {
            return false;
        }
        invicardLog.setShareNum(invicardLog.getShareNum() + num);
        invicardLog.setUpdateTime(new Date());
        return super.update(invicardLog, new Condition().eq(Columns.id, invicardLog.getId()));
    }
}
