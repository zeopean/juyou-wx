/**
 *
 */
package com.juyou.wx.util;

import com.google.gson.JsonArray;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author 马伟杰
 * @ClassName: GeneralUtil
 * @Description: 常用工具类
 * @date 2016年9月6日 上午10:49:46
 * modify zeopean
 */
public class GeneralUtil {

    /**
     * @param obj
     * @return boolean
     * @Title: isNull
     * @Description: 只判断 !=null
     * @author 马伟杰
     */
    public static boolean isNull(Object obj) {
        return null == obj;
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    /**
     * @param obj 输入对象 (Object,Long,String...等对象)
     * @return 不为空返回 true, 否则返回false
     * @Title: isObjNotNull
     * @Description: (判断Object 是否为空, 常用于字符串的判断)
     * @author 马伟杰
     */
    public static boolean isObjNotNull(Object obj) {
        return !isObjNull(obj);
    }


    public static boolean isObjNull(Object obj) {
        return obj == null || "".equals(obj.toString().trim());
    }


    /**
     * @param list
     * @return 如果list不为空 返回 true, 为空返回 false
     * @Title: isListNotNull
     * @Description: (判断 list 是否为空)
     * @author 马伟杰
     */
    public static boolean isListNotNull(List list) {
        if (list != null && !list.isEmpty()) {
            return true;
        }
        return false;
    }


    public static boolean isListNull(List list) {
        return !isListNotNull(list);
    }

    public static boolean isMultiListNotNull(List... lists) {
        if (lists == null) {
            return false;
        } else {
            for (List list : lists) {
                if (isListNull(list)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String getUuid() {

        return UUID.randomUUID().toString().replace("-", "");
    }


    /**
     * @param objects 传入的可变参数
     * @return boolean
     * @Title: isMultiNotNull
     * @Description: (判断可变参数是否为空 ， 只要一个为null 或 空字符串 ， 就返回 false, 否则返回true)
     * @author 马伟杰
     */
    public static boolean isMultiNotNull(Object... objects) {

        if (objects == null) {
            return false;
        } else {
            for (Object object : objects) {
                if (isObjNull(object)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 字段不为 null 或者 空字符串
     *
     * @param objects
     * @return
     */
    public static boolean isMultiNotNullAndNotEmpty(Object... objects) {

        if (objects == null) {
            return false;
        } else {
            for (Object object : objects) {
                if (isObjNull(object) || object.toString().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断非空并且不等于0， 常用与  判断 主键id  非空并且非0
     *
     * @param objects
     * @return
     */
    public static boolean isMultiNotNullAndNotZero(Object... objects) {

        if (objects == null) {
            return false;
        } else {
            for (Object object : objects) {
                if (isObjNull(object) || "0".equals(object.toString())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 参数不为 0, null, ""
     *
     * @param objects
     * @return
     */
    public static boolean isMultiNotNullAndNotZeroAndNotEmpty(Object... objects) {
        if (objects == null) {
            return false;
        } else {
            for (Object object : objects) {
                if (isObjNull(object) || "0".equals(object.toString()) || "".equals(object.toString())) {
                    return false;
                }
            }
        }
        return true;
    }


    public static boolean isMultiHasNullOrZero(Object... objects) {
        return !isMultiNotNullAndNotZero(objects);
    }


    public static boolean isObjNullOrZero(Object obj) {
        return isObjNull(obj) || "0".equals(obj.toString());
    }

    public static boolean isObjNotNullAndZero(Object obj) {
        return !isObjNullOrZero(obj);
    }

    /**
     * 判断非 0
     *
     * @param obj int ,long 等基本类型 整形数据
     * @return
     */
    public static boolean isObjEqualsZero(Object obj) {
        return isNotNull(obj) && "0".equals(obj.toString().trim());
    }

    public static boolean isObjNotZero(Object obj) {
        return !isObjEqualsZero(obj);
    }


    /**
     * @param objects
     * @return boolean
     * @Title: isMultiHasNull
     * @Description: 判断传进来的可变参数中 是否有null或者""
     * @author 马伟杰
     */
    public static boolean isMultiHasNull(Object... objects) {

        return !isMultiNotNull(objects);
    }


    /**
     * @param doubleVal
     * @return String
     * @Title: getFormatDouble
     * @Description: (格式化double值 位两位小数点数的字符串 eg : 0.00, 4.23)
     * @author 马伟杰
     */
    public static String getFormatDouble(Double doubleVal) {
        DecimalFormat decimalFormat = new DecimalFormat("######0.00");
        return decimalFormat.format(doubleVal);
    }


    /**
     * @param doubleVal
     * @return double
     * @Title: getDecimalFraction
     * @Description: 对double值 四舍五入
     * @author 马伟杰
     */
    public static double getDecimalFraction(Double doubleVal) {
        if (GeneralUtil.isNotNull(doubleVal)) {
            return new BigDecimal(doubleVal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return 0;
    }


    /**
     * 首字母转小写
     *
     * @param str
     * @return
     */
    public static String toLowerCaseFirstOne(String str) {
        if (Character.isLowerCase(str.charAt(0))) {
            return str;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString();
        }
    }


    /**
     * @param str
     * @return String
     * @Title: filterEmoji
     * @Description: 过滤表情
     * @author 马伟杰
     */
    public static String filterEmoji(String str) {

        if (isObjNull(str)) {
            return "";
        }
        String pattern = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";
        String reStr = "";
        Pattern emoji = Pattern.compile(pattern);
        Matcher emojiMatcher = emoji.matcher(str);
        str = emojiMatcher.replaceAll(reStr);
        return str;
    }


    public static String replaceBlankSpace(String str) {
        String pattern = "\\s{1}";
        String reStr = "+";
        Pattern emoji = Pattern.compile(pattern);
        Matcher blankSpaceMatcher = emoji.matcher(str);
        str = blankSpaceMatcher.replaceAll(reStr);
        return str;
    }


    /**
     * @param str
     * @return boolean
     * @Title: isHasEmoji
     * @Description: 有emoji表情
     * @author 马伟杰
     */
    public static boolean isHasEmoji(String str) {
        String pattern = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";
        String reStr = "";
        Pattern emoji = Pattern.compile(pattern);
        Matcher emojiMatcher = emoji.matcher(str);
        if (emojiMatcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * @param str
     * @return
     */
    public static String wipeBlankSpace(String str) {
        String pattern = "\\s+";
        String reStr = "";
        Pattern emoji = Pattern.compile(pattern);
        Matcher blankSpaceMatcher = emoji.matcher(str);
        str = blankSpaceMatcher.replaceAll(reStr);
        return str;
    }


    /**
     * 判断数组 非空，非空返回 true, 否则 返回false
     *
     * @param array
     * @return
     */
    public static boolean isArrayNotNull(Object[] array) {
        if (array != null && array.length > 0) {
            return true;
        }
        return false;
    }

    public static boolean isArrayNull(Object[] array) {
        return !isArrayNotNull(array);
    }


    /**
     * 四舍五入 取整数
     *
     * @param scoringRatio 得分百分数
     * @return
     */
    public static double getRound(double scoringRatio) {
        return Math.round(scoringRatio);
    }


    /**
     * 去掉其它的<>之间的东西
     *
     * @param content
     * @return
     */
    public static String removeHtmlTag(String content) {
        if (GeneralUtil.isObjNotNull(content)) {
            return content.replaceAll("\\<.*?>", "");
        }
        return "";
    }


    /**
     * @param list       需要根据 comparator 规则 去重 的 list ，无序 treeSet
     * @param comparator 自定义 类型 的比较器
     * @param <E>        类型
     * @return 去重的 list 数据
     */
    public static <E> List<E> getSetData(List<E> list, Comparator<E> comparator) {
        if (GeneralUtil.isListNull(list)) {
            return null;
        }
        Set<E> set = new TreeSet<>(comparator);
        set.addAll(list);
        return new ArrayList<E>(set);
    }


    /**
     * 根据 cache key 和 参数值后缀 获得 格式化 后的 cache key
     *
     * @param formerCacheKey    eg: cp/account/id/%s
     * @param suffixParamValues eg: id
     * @return
     */
    public static String getFormatKey(String formerCacheKey, Object... suffixParamValues) {
        return String.format(formerCacheKey, suffixParamValues);
    }


    /**
     * 判断 集合 是否为空
     *
     * @param map 集合
     * @param <k> map 的 key 类型
     * @param <v> map 的 value 类型
     * @return
     */
    public static <k, v> boolean isMapNotNull(Map<k, v> map) {
        if (isNotNull(map) && map.size() > 0) {
            return true;
        }
        return false;
    }


    public static <k, v> boolean isMapNull(Map<k, v> map) {
        return !isMapNotNull(map);
    }


    public static int getFirstIndex(int page, int pageSize) {
        return (page - 1) * pageSize;
    }


    /**
     * list 转 string  字符串
     *
     * @param list      列表list
     * @param separator 分隔符 ，常用： ,
     * @return
     */
    public static String listToString(List list, String separator) {
        if (isListNull(list)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                sb.append(list.get(i));
            } else {
                sb.append(list.get(i));
                sb.append(separator);
            }
        }
        return sb.toString();
    }


    /**
     * 判断 Gson 的 jsonArray 是否为空
     *
     * @param jsonArray
     * @return
     */
    public static boolean isJsonArrayNotNull(JsonArray jsonArray) {
        return isNotNull(jsonArray) && jsonArray.size() > 0;

    }


    public static String base64Encode(String origin) {
        try {
            return Base64.getUrlEncoder().encodeToString(origin.getBytes("utf-8"));
        } catch (Exception ex) {
            return null;
        }
    }


    public static String base64Decode(String origin) {
        try {
            byte[] asBytes = Base64.getUrlDecoder().decode(origin);
            return new String(asBytes, "utf-8");
        } catch (Exception ex) {
            return null;
        }
    }


    public static String decodeUrl(String url) throws UnsupportedEncodingException {
        String decodeUrl = URLDecoder.decode(url, "UTF-8");
        return decodeUrl;

    }


    public static <T> int getSubtractorNum(List<T> list, int totalNum) {
        if (GeneralUtil.isListNull(list)) {
            return totalNum;
        }
        return totalNum - list.size();
    }

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * 转换list 成 字符串
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> String changeListToCommaStr(List<T> list) {
        StringBuilder stringBuilder = new StringBuilder();
        if (isListNotNull(list)) {
            for (int i = 0; i < list.size(); i++) {
                Object obj = list.get(i);
                if (0 != i) {
                    stringBuilder.append("," + list.get(i));
                } else {
                    stringBuilder.append(list.get(i));
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 获取截取后的堆栈信息字符串
     *
     * @param stackTrace
     * @return
     */
    public static String getSubStringByStackTrace(String stackTrace) {
        String str = null;
        try {
            int index = stackTrace.indexOf(".java");
            for (int i = 0; i < 3; i++) {
                index = stackTrace.indexOf(".java", index + 5);
            }
            str = stackTrace.substring(0, index + 10);
        } catch (Exception e) {
            e.printStackTrace();
            str = stackTrace;
        }
        return str;
    }


}
