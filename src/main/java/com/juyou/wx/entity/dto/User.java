package com.juyou.wx.entity.dto;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-09-19
 */
@NoArgsConstructor
@Data
@TableName("jy_user")
public class User extends Model {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 对外接入OpenUserId
     */
    private String openid;
    /**
     * 用户类型：1 微信用户
     */
    @TableField("user_type")
    private Integer userType;
    /**
     * 用户姓名
     */
    private String nickname;
    /**
     * 1 男 ·2女 ，  默认0
     */
    private Integer gender;

    private Integer subscribe;
    /**
     * 头像Url
     */
    private String avatar;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 邮件
     */
    private String email;
    /**
     * 国家
     */
    private String country;
    /**
     * 省
     */
    private String province;
    /**
     * 城市
     */
    private String city;

    @TableField("subscribe_time")
    private Date subscribeTime;

    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 多个公众号之间用户帐号互通UnionID机制
     */
    private String unionid;


    @Override
    protected Serializable pkVal() {
        return null;
    }
}
