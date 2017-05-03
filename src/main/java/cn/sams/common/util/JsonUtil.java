package cn.sams.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 *JSON相关工具类
 *
 */
public class JsonUtil {

    private static ObjectMapper mapper = new ObjectMapper();
	static{
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		/**
		 * 忽略对象中的NULL值
		 */
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		/**
		 * 忽略MAP中的NULL值
		 */
		mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
	}
	
	
	/**
	 *Java对象 转 JSON 字符串
	 * @param object = 任意java对象
	 * @return 
	 */
	public static String toString(Object object){
//	
		String json = "";
		try {
			 json = mapper.writeValueAsString(object);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	
	/**
	 *JSON格式的字符串 转 List
	 * @param jsonStr
	 * @param t = 对象class
	 * @return
	 */
	public static <T> List<T> toList(String jsonStr,Class<T> t){
		if( ! Chk.spaceCheck(jsonStr)){
			return null;
		}
		JavaType type  = mapper.getTypeFactory().constructParametricType(List.class, t);
		List<T> list = null;
		try {
			list = mapper.readValue(jsonStr, type);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 *JSON格式字符串 转 Map
	 * @param jsonStr
	 * @param k = Map中 key的class
	 * @param t = Map 中 value 的class
	 * @return
	 */
	public static <K,T> Map<K,T> toMap(String jsonStr,Class<K> k,Class<T> t){
		if( ! Chk.spaceCheck(jsonStr)){
			return null;
		}
		JavaType type  = mapper.getTypeFactory().constructParametricType(Map.class,k, t);
		Map <K,T> map = null;
		try {
			map = mapper.readValue(jsonStr, type);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	/**
	 *JSON格式字符串 转 JAVA对象(此方法不能转换集合类型)
	 * @param jsonStr
	 * @param t = java对象的class
	 * @return
	 */
	public static <T> T toObject(String jsonStr,Class<T> t){
		if( ! Chk.spaceCheck(jsonStr)){
			return null;
		}
		T json = null;
		try {
			json = mapper.readValue(jsonStr, t);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return json;
	}
	
	/**
	 *JSON格式字符串 转 JAVA对象(此方法不能转换集合类型)
	 * @param jsonStr
	 * @param tr
	 * @return
	 */
	public static <T> T toObject(String jsonStr,TypeReference tr){
		if( ! Chk.spaceCheck(jsonStr)){
			return null;
		}
		T json = null;
		try {
			json = mapper.readValue(jsonStr, tr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

}
