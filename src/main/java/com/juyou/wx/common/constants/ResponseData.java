package com.juyou.wx.common.constants;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-06-26
 */
public enum ResponseData {

    REQUEST_SUCCESS(1, "请求接口成功"),
    REQUEST_FAIL(0, "请求接口失败"),

    /**
     * 用户相关
     */
    SIGNATURE_ERROR(1001, "签名错误！"),

    WX_CODE_ERROR(1002, "公众号编号错误！"),

    WX_ACCESS_TOKEN_ERROR(1003, "获取微信accessToken 失败!"),

    WX_SEND_KEFU_MSG_ERROR(1004, "发送微信客服消息失败!"),

    WX_CREATE_MENU_ERROR(1005, "微信创建菜单失败!"),
    WX_GET_MENU_ERROR(1006, "微信获取菜单失败!"),
    WX_DELETE_MENU_ERROR(1007, "微信删除菜单失败!"),

    WX_CREATE_QRCODE_ERROR(1008, "微信创建二维码失败！"),
    WX_QRCODE_INVID_ERROR(1008, "微信二维码参数非法失败！"),

    WX_UPLOAD_ERROR(1009, "微信上传文件失败！"),
    WX_IO_UPLOAD_ERROR(1009, "微信上传IO异常！"),



    SERVER_INNER_ERROR(5000, "系统繁忙，请重试"),
    DingTalk_FAIL_ERROR(5003, "钉钉告警失败!"),



    REQUEST_ERROR(9999, "服务异常！");

    private int code;
    private String msg;

    ResponseData(int code, String msg) {
        this.code = code;
        this.msg = msg;
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
}
