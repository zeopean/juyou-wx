package com.juyou.wx.service.card.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-09-27
 */
@Data
@NoArgsConstructor
public class EventMsg {

    private String wxCodes;
    private List<Msg> messages;

}
