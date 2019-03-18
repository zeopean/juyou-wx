package com.juyou.wx.entity.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-09-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("jy_invicard_log")
public class InvicardLog implements Serializable {

    @Id
    private long id;
    private int code;
    @TableField("user_id")
    private long userId;

    private String openid;
    private String unionid;

    @TableField("media_id")
    private String mediaId;
    @TableField("share_num")
    private int shareNum;
    @TableField("is_active_into")
    private int isActiveInto;
    @TableField("is_new_user")
    private int isNewUser;


    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;
}
