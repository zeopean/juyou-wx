package com.juyou.wx.vos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created with idea
 * Description: token vo
 *
 * @author zeopean
 * Date: 2018-06-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenVo implements Serializable {

    private String wxCode;
    private String token;
    private int expire;
}
