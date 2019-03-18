package com.juyou.wx.service.card.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-09-22
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PicFont {
    private int size;
    private String font;
    private PicColor picColor;
    private String content;
    private int lineHeight;
    private int lineWidth;
    private String defaultContent;
    private int isBold;
}
