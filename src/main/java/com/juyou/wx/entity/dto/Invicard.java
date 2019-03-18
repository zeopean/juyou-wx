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
@TableName("jy_invicard")
public class Invicard implements Serializable {

    @Id
    private long id;
    private int code;
    @TableField("user_id")
    private long userId;
    @TableField("share_user_id")
    private long shareUserId;
    private int views;
    @TableField("is_receive")
    private int isReceive;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;

}
