## 介绍

该项目利用微信提供的接口，实现了裂变邀请卡的功能，可用于运营活动。


## 框架

```

1.spring-boot


2.微信开发工具：weixin-java-mp

	<!-- 微信工具 -->
	<dependency>
	    <groupId>com.github.binarywang</groupId>
	    <artifactId>weixin-java-mp</artifactId>
	    <version>3.2.0</version>
	</dependency>
	<dependency>
	    <groupId>com.github.binarywang</groupId>
	    <artifactId>weixin-java-cp</artifactId>
	    <version>3.2.0</version>
	</dependency>


3.mybatis-plus

	<dependency>
	    <groupId>com.baomidou</groupId>
	    <artifactId>mybatis-plus-boot-starter</artifactId>
	    <version>2.3.1</version>
	</dependency>
	
	<dependency>
	    <groupId>com.baomidou</groupId>
	    <artifactId>mybatis-plus</artifactId>
	    <version>2.3.1</version>
	</dependency>
    
    

```


## 注意事项

```

1. mybatis-plus 在使用中，没用到 Mapper，而是用它原有的 Service 封装成了 dao 

2. 项目中使用了lombok，以减少代码量


```


## 项目部署

```

1. 克隆项目

2. 通过 mvn 构建项目

3. 执行 juyou-wx/sql/20180915-邀请卡-ddl.sql

4. 修改配置文件，本地开发环境为application-dev.properties，线上正式环境为 application-prod.properties，进行 mysql 的设置，邮箱、日志的配置

5. application-wx.properties 为微信相关配置，可以在微信公众号后台获得

6. 邀请卡的客服消息配置在 card.conf, 公众号的菜单配置在 card.menu, 邀请卡的底图，在 card.image

7. mybatis 的 mapping 文件在 mapping 中


```



