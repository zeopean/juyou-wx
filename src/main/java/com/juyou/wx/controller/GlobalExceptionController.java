package com.juyou.wx.controller;


import com.juyou.wx.common.JsonResponse;
import com.juyou.wx.common.constants.ResponseData;
import com.juyou.wx.util.logger.LogUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常信息捕获
 * @author jmmy
 */
@ControllerAdvice
public class GlobalExceptionController {


    @ExceptionHandler(Exception.class)
    @ResponseBody
    protected JsonResponse handleException(Exception ex, HttpServletRequest req) {
        //发送邮件通知 todo

        String msg = "request global error";
        LogUtil.error(ex, ResponseData.SERVER_INNER_ERROR.getCode(), msg);
        return  new JsonResponse().failResponse();
    }

}
