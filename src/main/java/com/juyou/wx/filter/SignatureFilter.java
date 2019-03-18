package com.juyou.wx.filter;

import com.juyou.wx.common.JsonResponse;
import com.juyou.wx.common.constants.Common;
import com.juyou.wx.common.constants.ResponseData;
import com.juyou.wx.util.ApiSignUtil;
import com.juyou.wx.util.JsonUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-06-26
 */
//@WebFilter(filterName = "signatureFilter", urlPatterns = "/*")
public class SignatureFilter implements Filter {

    private final static String SIGN = "sign";
    private final static String TIMESTAMP = "timestamp";

    @Value(value = "${signature.check}")
    String signatureCheck;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // 是否验证
        if (Common.NO_STR.equals(signatureCheck)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        Map<String, String[]> params = servletRequest.getParameterMap();
        String sign = ApiSignUtil.getSign(params, "todo apiSecret ", servletRequest.getParameter(TIMESTAMP));

        String fromSign = servletRequest.getParameter(SIGN);

        if (sign.equals(fromSign)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST");
        response.addHeader("Access-Control-Allow-Origin", "allowOrigin");
        PrintWriter writer = response.getWriter();

        try {

            JsonResponse error = new JsonResponse(ResponseData.SIGNATURE_ERROR);
            writer.write(JsonUtil.toJSONString(error));

        } finally {
            IOUtils.closeQuietly(writer);
        }

        filterChain.doFilter(request, response);
        return;
    }

    @Override
    public void destroy() {

    }
}
