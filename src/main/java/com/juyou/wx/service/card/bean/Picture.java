package com.juyou.wx.service.card.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-08-06
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Picture {

    private int width;
    private int height;
    private int startX;
    private int startY;
    private String filePath;
    private String urlPath;
    private int isBackground;
    private int isCircle;
    private int isFont;
    private PicFont picFont;
    private PicColor picColor;
    private String type;

}
