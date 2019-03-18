package com.juyou.wx.vos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created with idea
 * Description: 微信配置信息 vo
 *
 * @author zeopean
 * Date: 2018-06-28
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WxInfoVo implements Serializable {

    private String wxCode;
    private String wxName;
    private String notifyUrl;

}
