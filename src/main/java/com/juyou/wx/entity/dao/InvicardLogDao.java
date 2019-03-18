package com.juyou.wx.entity.dao;

import com.baomidou.mybatisplus.service.IService;
import com.juyou.wx.entity.dto.InvicardLog;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-09-19
 */
public interface InvicardLogDao extends IService<InvicardLog> {

    /**
     * 查找数据
     *
     * @param userId
     * @param code
     * @return
     */
    InvicardLog findOne(Long userId, String code);

    /**
     * 递增
     *
     * @param id
     * @param num
     * @return
     */
    boolean increment(Long id, int num);
}
