package com.juyou.wx.service;

import com.juyou.wx.common.JsonResponse;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-09-27
 */
public interface IMenuService {

    /**
     * 创建订单
     *
     * @param wxCode
     * @return
     */
    JsonResponse create(String wxCode);

}
