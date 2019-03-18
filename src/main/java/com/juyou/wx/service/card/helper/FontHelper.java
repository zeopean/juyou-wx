package com.juyou.wx.service.card.helper;

import com.juyou.wx.util.GeneralUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created with idea
 * Description: 字体工具
 *
 * @author zeopean
 * Date: 2018-08-29
 */
@Component
public final class FontHelper {

    private final static String PINGFANG_MEDIUM = "PingFang SC Medium";
    private final static String PINGFANG_HEAVY = "PingFang SC Heavy";
    private final static String PINGFANG_LIGHT = "PingFang SC Light";


    private static String fontPingFang;

    @Value("${font.regular.path}")
    public void setFontPingFang(String fontPingFang) {
        FontHelper.fontPingFang = fontPingFang;
    }

    public static Font getMediumFont(int fontSize) {
        if (GeneralUtil.isObjNotNull(fontPingFang)) {
            File file = new File(fontPingFang);
            try {
                FileInputStream fi = new FileInputStream(file);
                BufferedInputStream fb = new BufferedInputStream(fi);
                Font font = Font.createFont(Font.TRUETYPE_FONT, fb);
                return font.deriveFont(Font.PLAIN, fontSize);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new Font(PINGFANG_MEDIUM, Font.PLAIN, fontSize);
    }

    public static Font getBoldFont(int fontSize) {
        if (GeneralUtil.isObjNotNull(fontPingFang)) {
            File file = new File(fontPingFang);
            try {
                FileInputStream fi = new FileInputStream(file);
                BufferedInputStream fb = new BufferedInputStream(fi);
                Font font = Font.createFont(Font.TRUETYPE_FONT, fb);
                return font.deriveFont(Font.BOLD, fontSize);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new Font(PINGFANG_HEAVY, Font.BOLD, fontSize);
    }

    public static Font getLightFont(int fontSize) {
        return new Font(PINGFANG_LIGHT, Font.PLAIN, fontSize);
    }

}
