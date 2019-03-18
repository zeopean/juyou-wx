package com.juyou.wx.util.logger;

import com.juyou.wx.common.constants.Common;
import com.juyou.wx.common.constants.ResponseData;
import com.juyou.wx.util.GeneralUtil;
import com.juyou.wx.util.HttpUtil;
import com.juyou.wx.util.email.EmailBean;
import com.juyou.wx.util.email.EmailUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * 统一日志类
 * Created by meast on 2017/7/14.
 */
@Component
public class LogUtil {
    private static final Logger LOG = LoggerFactory.getLogger(LogUtil.class);

    private static EmailUtil emailUtil;
    private static EmailBean emailBean;

    @Autowired
    public void setEmailUtil(EmailUtil emailUtil) {
        LogUtil.emailUtil = emailUtil;
    }

    @Autowired
    public void setEmailBean(EmailBean emailBean) {
        LogUtil.emailBean = emailBean;
    }

    /**
     * @param msg
     * @param params
     */
    public static void info(String msg, Object... params) {
        info(ResponseData.REQUEST_SUCCESS.getCode(), msg, params);
    }

    /**
     * @param msg
     * @param params
     */
    public static void info(int code, String msg, Object... params) {
        LOG.info(formatLog(code, msg), params);
    }

    /**
     * 打印业务上错误日志
     *
     * @param msg
     * @param code
     * @param params
     */

    public static void error(int code, String msg, Object... params) {
        LOG.error(formatLog(code, msg), params);
        //发送邮件
        String str = String.format(msg, params);

        emailBean.setSubject("服务异常！");
        emailBean.setContent(str);
        emailUtil.sendMail(emailBean);

    }

    public static void error(Exception e, int code, String msg, Object... params) {
        //  同时把 exception  记录进去 tb_cp_exception
        LOG.error(formatLog(code, msg), params);
        LOG.error(Common.FAIL, formatLog(code, msg), params, e);

        //发送邮件
        emailBean.setSubject("服务异常！");
        String stackTrace = ExceptionUtils.getStackTrace(e);
        String str = GeneralUtil.getSubStringByStackTrace(stackTrace);
        emailBean.setContent(str);
        emailUtil.sendMail(emailBean);
    }


    public static void error(Exception e, ResponseData responseData, Object... params) {
        //  同时把 exception  记录进去 tb_cp_exception
        if (GeneralUtil.isNotNull(responseData)) {
            LOG.error(formatLog(responseData.getCode(), responseData.getMsg()), params);
            LOG.error(Common.FAIL, formatLog(responseData.getCode(), responseData.getMsg()), params, e);

            //发送邮件
            emailBean.setSubject("服务异常！");
            String stackTrace = ExceptionUtils.getStackTrace(e);
            String str = GeneralUtil.getSubStringByStackTrace(stackTrace);
            emailBean.setContent(str);
            emailUtil.sendMail(emailBean);
        }
    }

    /**
     * 警告级别日志
     *
     * @param msg    信息提示
     * @param code   code 代码
     * @param params 数据记录 可变参数
     */
    public static void warn(int code, String msg, Object... params) {
        LOG.warn(formatLog(code, msg), params);
    }



    /**
     * 日志输出
     *
     * @param message
     * @param code
     * @return
     */
    private static String formatLog(int code, String message) {

        StringBuilder ret = new StringBuilder();
        ServletRequestAttributes attributes = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes());
        HttpServletRequest req = null;
        if (null != attributes) {
            req = attributes.getRequest();
            ret.append(", \"ip\":").append("\"").append(req.getRemoteAddr()).append("\"");
            ret.append("\"url\":").append("\"").append(HttpUtil.getRequestURL(req)).append("\"");
        }

        ret.append(", \"browsertype\":").append("\"").append(RequestContext.getContextParamsByKey("browsertype")).append("\"");
        ret.append(", \"ua\":").append("\"").append(RequestContext.getContextParamsByKey("ua")).append("\"");
        ret.append(", \"sessionid\":").append("\"").append(RequestContext.getContextParamsByKey("sessionid")).append("\"");
        ret.append(", \"use_time\":").append("\"").append(RequestContext.getRequestTime()).append("\"");
        Map<String, String> map = RequestContext.getStatParams();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                ret.append(", \"" + entry.getKey() + "\":").append("\"").append(entry.getValue()).append("\"");
            }
        }
        ret.append(", \"code\":").append("\"").append(code).append("\"");
        ret.append(", \"message\":").append("\"").append(message).append("\"");

        return ret.toString();
    }

}
