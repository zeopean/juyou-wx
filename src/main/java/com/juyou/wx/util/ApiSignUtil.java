package com.juyou.wx.util;


import java.util.Map;
import java.util.TreeMap;

/**
 * @author zeopean
 * @since 2018-04-04
 * api接口签名工具
 */
public class ApiSignUtil {

    /**
     * 生成 参数验证签名
     *
     * @param params    请求的参数
     * @param apiSecret api秘钥
     */
    public static String getSign(String[] params, String apiSecret) {
        if (GeneralUtil.isArrayNull(params) || GeneralUtil.isNull(apiSecret)) {
            return "";
        }
        //获取传递过来的参数
        StringBuilder queryStr = new StringBuilder();
        for (String val : params) {
            queryStr.append(val);
            queryStr.append(",");
        }
        queryStr.append(apiSecret);
        return MD5Util.encodeByMD5(queryStr.toString()).toLowerCase();
    }


    /**
     * 生成 参数验证签名
     *
     * @param params    请求的参数
     * @param apiSecret api秘钥
     * @param timestamp 时间戳
     */
    public static String getSign(Map<String, String[]> params, String apiSecret, String timestamp) {
        if (GeneralUtil.isMapNull(params) || GeneralUtil.isNull(apiSecret) || GeneralUtil.isNull(timestamp)) {
            return "";
        }
        //获取传递过来的参数
        Map<String, String> newParams = new TreeMap<>();
        for (String key : params.keySet()) {
            if (key.equals("body") || key.equals("timestamp")) {
                continue;
            } else {
                newParams.put(key, params.get(key)[0]);
            }
        }
        StringBuilder queryStr = new StringBuilder();
        for (String key : newParams.keySet()) {
            queryStr.append(key);
            queryStr.append("=");
            queryStr.append(newParams.get(key));
            queryStr.append(",");
        }
        queryStr.append(timestamp);
        queryStr.append(apiSecret);
        return MD5Util.encodeByMD5(queryStr.toString()).toLowerCase();
    }



}
