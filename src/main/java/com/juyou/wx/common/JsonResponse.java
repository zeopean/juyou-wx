package com.juyou.wx.common;

import com.juyou.wx.common.constants.ResponseData;

/**
 * 后端响应体
 * @author zeopean
 */
public class JsonResponse {

    /**
     * 请求成功状态码
     */
    public static final int SUCCESS_CODE = 1;

    public static final String SUCCESS_MSG = "请求成功！";

    /**
     * 请求失败状态码
     */
    public static final int FAIL_CODE = 0;

    public static final String FAIL_MSG = "请求失败！";


    /**
     * 响应码
     */
    private int code;
    /**
     * 响应信息
     */
    private String msg;
    /**
     * 响应数据
     */
    private Object data;

    public JsonResponse() {
    }


    public JsonResponse(ResponseData responseData) {
        this.msg = responseData.getMsg();
        this.code = responseData.getCode();
    }

    public JsonResponse(int code) {
        this.code = code;
    }


    public JsonResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResponse(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonResponse successResponse() {
        this.code = SUCCESS_CODE;
        this.msg = SUCCESS_MSG;

        return this;
    }

    /**
     * 成功响应
     *
     * @param data
     * @return
     */
    public JsonResponse successResponse(Object data) {
        this.code = SUCCESS_CODE;
        this.msg = SUCCESS_MSG;
        this.data = data;

        return this;
    }

    public JsonResponse failResponse() {
        this.code = FAIL_CODE;
        this.msg = FAIL_MSG;
        return this;
    }

    /**
     * 失败响应
     *
     * @param data
     * @return
     */
    public JsonResponse failResponse(Object data) {
        this.code = FAIL_CODE;
        this.msg = FAIL_MSG;
        this.data = data;
        return this;
    }



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
