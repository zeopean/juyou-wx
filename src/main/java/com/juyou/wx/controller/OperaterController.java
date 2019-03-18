package com.juyou.wx.controller;

import com.juyou.wx.common.JsonResponse;
import com.juyou.wx.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-09-27
 */
@RestController
@RequestMapping("/oper")
public class OperaterController {

    @Autowired
    private IMenuService menuService;

    @RequestMapping("/update-menu")
    public JsonResponse updateMenu(String wxCode) {
        return menuService.create(wxCode);
    }

}
