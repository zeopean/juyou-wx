package com.juyou.wx.config.wx;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-06-25
 */
public enum WechatType {

    Default(1, "default", "测试微信公众号"),

    WxMp(2, "wxmp", "北鱼公众号");


    WechatType(int id, String wxCode, String wxName) {
        this.id = id;
        this.wxCode = wxCode;
        this.wxName = wxName;
    }

    private int id;
    private String wxCode;
    private String wxName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWxCode() {
        return wxCode;
    }

    public void setWxCode(String wxCode) {
        this.wxCode = wxCode;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    /**
     * 通过 wxCode 获取 账号名字
     * @param wxCode
     * @return
     */
    public static String getWxNameByWxCode(String wxCode) {
        for (WechatType wechatType : values()) {
            if (wechatType.getWxCode().equals(wxCode)) {
                return wechatType.getWxName();
            }
        }
        return "";
    }


}
