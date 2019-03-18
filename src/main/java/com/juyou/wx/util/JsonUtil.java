/**
 * 
 */
package com.juyou.wx.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/** 
* @ClassName: JsonUtil
* @Description:  JSON工具类
* @author 马伟杰
* @date 2017-7-17 14:44:42
*/
public class JsonUtil {
	
	
	/**
	 * 
	* @Title: parseArray
	* @Description: ( 解析数组字符串成 List)
	* @param jsonArrayStr		json格式的数组字符串
	* @param clazz				json对应的bean的 class
	* @return         List<T>   解析完成的  list集合
	* @author 马伟杰
	 */
	public static <T> List<T> parseArray(String jsonArrayStr,Class<T> clazz){
		return JSON.parseArray(jsonArrayStr, clazz);
	}
	
	/**
	 * 
	* @Title: parseObject
	* @Description: ( 解析对象字符串 成 对象)
	* @param jsonObjStr		 	json格式的对象字符串
	* @param clazz				json对应的 bean的class
	* @return         T 		对象
	* @author 马伟杰
	 */
	public static <T> T parseObject(String jsonObjStr,Class<T> clazz){
		return JSON.parseObject(jsonObjStr, clazz);
	}


	/**
	 *  将 json字符串 转换 成 JSONObject
	 * @param jsonStr
	 * @return
	 */
	public static JSONObject parseObject(String jsonStr){
		if(GeneralUtil.isObjNull(jsonStr)){
			return new JSONObject();
		}
		return JSON.parseObject(jsonStr);
	}
	
	/**
	 * 
	* @Title: toJSONString
	* @Description: (把Object对象 转换成json字符串)
	* @param obj		对象
	* @return         String  json字符串 
	* @author 马伟杰
	 */
	public static String toJSONString(Object obj){
		return JSON.toJSONString(obj);
	}


	/**
	 *  判断 json数组 是否为空
	 * @param jsonArray
	 * @return
	 */
	public static boolean isJsonArrayNull(JSONArray jsonArray){
		return null == jsonArray || 0 == jsonArray.size();

	}

	/**
	 *  判断 json 数组 是否不为空
	 * @param jsonArray
	 * @return
	 */
	public static boolean isJsonArrayNotNull(JSONArray jsonArray){
		return !isJsonArrayNull(jsonArray);

	}

	
	
	

	
	

}
