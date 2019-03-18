
package com.juyou.wx.util.logger;


import com.juyou.wx.util.GeneralUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class RequestContext {

    /**
     * 线程变量
     */
    public static ThreadLocal<RequestContext> current = new ThreadLocal<>();
    /**
     * 自定义统计参数
     */
    private Map<String, String> customStatParams = new TreeMap<>();

    /**
     * 上下文参数
     */
    private Map<String, Object> contextParams = new HashMap<>();



    /**
     * 添加统计参数
     *
     * @param key
     * @param value
     */
    public static void addStatParams(String key, String value) {
        getStatParams().put(key, value);
    }

    /**
     * 添加上下文参数
     *
     * @param key
     * @param value
     */
    public static void addContextParams(String key, Object value) {
        getContextParams().put(key, value);
    }


    /**
     * 获取统计参数对象
     *
     * @return
     */
    public static Map<String, String> getStatParams() {
        if (current.get() == null) {
            return new HashMap<>();
        }
        return current.get().customStatParams;
    }

    /**
     * 获取上下文参数对象
     *
     * @return
     */
    public static Map<String, Object> getContextParams() {
        if (current.get() == null) {
            return new HashMap<>();

        }
        return current.get().contextParams;
    }

    public static long getRequestTime() {
        if (GeneralUtil.isObjNull(getContextParamsByKey("beginTime"))) {
            return 0;
        }
        return System.currentTimeMillis() - Long.valueOf(getContextParamsByKey("beginTime"));
    }


    public static String getContextParamsByKey(String key) {
        if (current.get() == null) {
            return "";

        }
        Object value = current.get().contextParams.get(key);
        if (value == null) {
            return "";
        }
        return String.valueOf(value);
    }
}
