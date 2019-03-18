package com.juyou.wx.service.card.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created with idea
 * Description: 邀请卡配置
 *
 * @author zeopean
 * Date: 2018-09-22
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CardConfig {
    private int code;
    private int defaultSubscribeCode;
    private String name;
    private String startTime;
    private String endTime;
    private int inviteNum;
    private String canEventType;
    private List<Success> success;
    private List<Picture> pictures;
    private List<ScannerMsg> scannerMsgs;
    private List<SharerMsg> sharerMsgs;

}
