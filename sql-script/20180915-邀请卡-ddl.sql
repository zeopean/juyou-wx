
CREATE TABLE `jy_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `openid` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '对外接入OpenUserId',
  `user_type` int(10) NOT NULL DEFAULT '0' COMMENT '用户类型：1 微信用户',
  `nickname` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户姓名',
  `gender` tinyint(2) NOT NULL DEFAULT '0' COMMENT '1 男 ·2女 ，  默认0',
  `subscribe` tinyint(2) NOT NULL DEFAULT '0' COMMENT '1 关注 0 未关注 ，默认0',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像Url',
  `mobile` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机',
  `email` varchar(70) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮件',
  `country` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '国家',
  `province` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '省',
  `city` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '城市',
  `subscribe_time` datetime COMMENT '关注时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `unionid` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '多个公众号之间用户帐号互通UnionID机制',
  PRIMARY KEY (`id`),
  KEY `inx_nickname` (`nickname`),
  KEY `inx_mobile` (`mobile`),
  KEY `inx_uninoid` (`unionid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';


-- 邀请卡
CREATE TABLE `jy_invicard` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL COMMENT '邀请卡类型code',
  `user_id` bigint(20) NOT NULL COMMENT '用户的user_key',
  `share_user_id` bigint(20) NOT NULL COMMENT '扫码的分享用户user_id, 用 “,”分割',
  `views` int(11) NOT NULL DEFAULT '0' COMMENT '好友浏览扫码次数',
  `is_receive` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否领取 1 表示领取， 0 表示未领取',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `inx_user` (`user_id`,`code`) USING BTREE,
  KEY `inx_code_receive` (`code`,`is_receive`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 邀请卡记录
CREATE TABLE `jy_invicard_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户 id',
  `openid` varchar(64) NOT NULL COMMENT 'openid',
  `unionid` varchar(64) COMMENT 'unionid',
  `code` int(11) DEFAULT '0' COMMENT '邀请卡类型code',
  `media_id` varchar(64) COMMENT '微信媒体id',
  `share_num` int(11) NOT NULL DEFAULT '0' COMMENT '单个扫码数',
  `is_active_into` tinyint(2) DEFAULT '0' COMMENT '是否从活动入口进入 1 是，0 否',
  `is_new_user` tinyint(2) DEFAULT '0' COMMENT '是否是新用户 1 是， 0 否',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `uni_card_media` (user_id, `code`, `media_id`),
  KEY `uni_card_newUser` (`code`, `is_new_user`),
  KEY `uni_card_activeInto` (`code`, `is_active_into`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

