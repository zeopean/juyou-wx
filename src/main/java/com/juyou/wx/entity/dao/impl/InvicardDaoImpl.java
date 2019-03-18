package com.juyou.wx.entity.dao.impl;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.juyou.wx.common.constants.Columns;
import com.juyou.wx.entity.dao.InvicardDao;
import com.juyou.wx.entity.dto.Invicard;
import com.juyou.wx.entity.mapper.InvicardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author zeopean
 * @since 2018-09-20
 */
@Primary
@Service
public class InvicardDaoImpl extends ServiceImpl<InvicardMapper, Invicard> implements InvicardDao {

    @Autowired
    private InvicardMapper invicardMapper;

    @Override
    public Invicard findOne(int code, long userId, long shareUserId) {
        return super.selectOne(new Condition().eq(Columns.code, code).eq(Columns.userId, userId).eq(Columns.shareUserId, shareUserId));
    }

    @Override
    public Invicard findOneForSharer(int code, long shareUserId) {
        return super.selectOne(new Condition().eq(Columns.code, code).eq(Columns.shareUserId, shareUserId).orderBy(Columns.id, false));
    }

    @Override
    public Invicard findOneForScanner(int code, long userId) {
        return super.selectOne(new Condition().eq(Columns.code, code).eq(Columns.userId, userId).orderBy(Columns.id, false));
    }

    @Override
    public boolean checkIsReceive(int code, long userId) {
        Invicard invicard = super.selectOne(new Condition().eq(Columns.code, code).eq(Columns.userId, userId).eq(Columns.isReceive, 1));
        if (null == invicard) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkIsScan(int code, long userId) {
        Invicard invicard = findOneForScanner(code, userId);
        if (null == invicard) {
            return false;
        }
        return true;
    }

    @Override
    public int countScanNum(int code, long shareUserId) {
        return selectCount(new Condition().eq(Columns.code, code).eq(Columns.shareUserId, shareUserId));
    }

    @Override
    public boolean create(int code, long userId, long shareUserId) {
        Invicard invicard = findOne(code, userId, shareUserId);
        if (null != invicard) {
            return true;
        }
        invicard = new Invicard();
        invicard.setCode(code);
        invicard.setUserId(userId);
        invicard.setShareUserId(shareUserId);
        invicard.setCreateTime(new Date());
        invicard.setUpdateTime(new Date());
        invicard.setViews(1);
        return insert(invicard);
    }

    @Override
    public boolean increment(Invicard invicard) {
        invicard.setViews(invicard.getViews() + 1);
        return update(invicard, new Condition().eq(Columns.id, invicard.getId()));
    }

    @Override
    public int userTopNum(int code, long shareUserId) {
        return (int)invicardMapper.userTopNum(code, shareUserId).getId();
    }
}
