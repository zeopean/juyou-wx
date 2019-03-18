package com.juyou.wx.service.card.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-09-27
 */
@Data
@NoArgsConstructor
public class Msg implements Serializable {

    private String name;
    private String type;
    private String title;
    private String description;
    private String url;
    private String imageUrl;
    private String content;
    private String mediaId;
    private String appid;
    private String pagePath;
    private String group;
    // 1,2,3 表示第1，2，3 个用户扫码是，发送该消息
    private String index;

    // 默认的消息
    private int isDefault;

    // 只发送一次
    private int onlyOnce;

}
