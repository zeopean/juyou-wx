package com.juyou.wx.service.card.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;

import java.util.List;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-09-23
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Success {
    private String type;
    private String content;
    private List<WxMpKefuMessage.WxArticle> wxArticles;
}
