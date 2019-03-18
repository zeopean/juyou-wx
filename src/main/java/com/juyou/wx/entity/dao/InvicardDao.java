package com.juyou.wx.entity.dao;

import com.baomidou.mybatisplus.service.IService;
import com.juyou.wx.entity.dto.Invicard;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-09-19
 */
public interface InvicardDao extends IService<Invicard> {

    Invicard findOne(int code, long userId, long shareUserId);

    /**
     * 查找分享者的数据
     *
     * @param code
     * @param shareUserId
     * @return
     */
    Invicard findOneForSharer(int code, long shareUserId);

    /**
     * 查找扫码者的数据
     *
     * @param code
     * @param userId
     * @return
     */
    Invicard findOneForScanner(int code, long userId);

    boolean checkIsReceive(int code, long userId);

    boolean checkIsScan(int code, long userId);

    int countScanNum(int code, long userId);

    /**
     * 创建新记录
     *
     * @param code
     * @param userId
     * @param shareUserId
     * @return
     */
    boolean create(int code, long userId, long shareUserId);

    /**
     * 递增
     *
     * @param invicard
     * @return
     */
    boolean increment(Invicard invicard);

    /**
     * 用户的排名
     *
     * @param code
     * @param shareUserId
     * @return
     */
    int userTopNum(int code, long shareUserId);

}
