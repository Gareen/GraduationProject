package cn.sams.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class StringUtil {

	/**
	 * 比较两个字符串
	 * null = 空字符串
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equals(String str1,String str2){
		return nullToEmptyString(str1).equals(nullToEmptyString(str2));
	}


	public static String nvl(String str,String replace,boolean includeEmptyString){
		return ( str == null || ( includeEmptyString ? "".equals(str) : false ) ) ? replace : str;
	}
	
	public static String nvl(String str,String replace){
		return nvl(str,replace,false);
	}
	
	public static String decode(String str,String value,String yreplace,String nreplace){
		return ( value == null ?   str == value : value.equals(str) ) ? yreplace : nreplace;
	}
	
	public static String nullToEmptyString(Object str){
		return str == null ? "" : str.toString();
	}
	
	public static String fromCamelCase(String str){
		if( ! Chk.spaceCheck(str)){
			return "";
		}
		return str.replaceAll("(\\S)([A-Z])", "$1_$2").toLowerCase();
	}
	
	
	public static String toCamelCase(String str){
		if( ! Chk.spaceCheck(str)){
			return "";
		}
		
		Matcher matcher = Pattern.compile("(\\S)[_](\\S)").matcher(str);
		StringBuffer sb = new StringBuffer();
		while(matcher.find()){
		   matcher.appendReplacement(sb,  matcher.group(1) + matcher.group(2).toUpperCase());  
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

}
