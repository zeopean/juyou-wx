package com.juyou.wx.vos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created with idea
 * Description: 二维码 vo
 *
 * @author zeopean
 * Date: 2018-06-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrcodeVo implements Serializable {

    private String wxCode;
    private String eventKey;
    private String qrcodeUrl;
    private String expireTime;

}
