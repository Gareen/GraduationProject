package cn.sams.common.util;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 * @author wangsq
 *
 */
public class Chk {

	public static boolean isDirection(String direction){
		return "A".equals(direction) || "D".equals(direction);
	}



	public static boolean mailCheck(String mail){
		if(!Chk.spaceCheck(mail)){
			return true;
		}
		String r = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		boolean rtn = Pattern.matches(r, mail);
		return rtn;
	}



	public static boolean integerCheck(String str,String type){

		if( ! spaceCheck(str) || ! spaceCheck(str)){
			return false;
		}


		List<String> rs = new ArrayList<String>();

		if(type.indexOf("0") >= 0){
			rs.add("^[0]$");
		}

		if(type.indexOf("+") >= 0){
			rs.add("^[1-9][0-9]*$");
		}

		if(type.indexOf("-") >= 0){
			rs.add("^[-][1-9][0-9]*$");
		}

		boolean rtn = false;
		for(String r : rs){
			rtn = Pattern.matches(r, str);
			if(rtn){
				break;
			}
		}
		return rtn;

	}



	public static boolean integerCheck(String str){
		return integerCheck(str, "-0+");
	}

	public static boolean minCheck(String str,int len){
		str = str == null ? "" : str;
		return str.length() >= len ;
	}

	public static boolean maxCheck(String str,int len){
		str = str == null ? "" : str;
		return str.length() <= len ;
	}

	public static boolean lengthCheck(String str,int min,int max){
		return minCheck(str,min) && maxCheck(str,max);

	}

	/**
	 * 严格要求 的数字检查
	 * @param str
	 * @return
	 */
	public static boolean numberCheck(String str){
		if( ! spaceCheck(str)){
			return false;
		}

		BigDecimal b = null;
		try{
			b = new BigDecimal(str);
		}catch(Exception e){
			return false;
		}
		return b.toString().equals(str);


//		String r = "^[0-9]+([.][0-9]+)?$";
//		boolean rtn = Pattern.matches(r, str.trim());
//		return rtn;
	}

	/*
	 * 区号+座机号码+分机号码
	 */
	public static boolean phoneCheck(String str){
		if(!spaceCheck(str)){
			return true;
		}

		String r  ="^(0[0-9]{2,3}\\-)?([0-9]{7,8})+(\\-[0-9]{1,4})?$";
		boolean rtn = Pattern.matches(r, str);
		return rtn;
	}

	public static boolean cellPhoneCheck(String str){
		if(!spaceCheck(str)){
			return true;
		}
		String r  ="^[0-9]{11}$";
		boolean rtn = Pattern.matches(r, str);
		return rtn;
	}


	/*
	 * 只含有汉字、数字、字母、下划线不能以下划线开头和结尾 长度3-16位
	 */
	public static boolean usernameCheck(String str){
		String r = "^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$";
		boolean rtn = Pattern.matches(r, str);
		if(rtn){
			rtn &= minCheck(str, 3);
			rtn &= maxCheck(str,16);
		}
		return rtn;
	}

	/*
	 * 只含有字母、数字长度3-16位
	 */
	public static boolean passwordCheck(String str){
		String r = "^[a-zA-Z0-9]{3,16}$";
		boolean rtn = Pattern.matches(r, str);
		return rtn;
	}
	
	
	static final String [] illegalCharacter = new String [] {
		"'","\"",">","<","\\"
	};
	
	public static boolean illegalCharacterCheck(String str){
		if(spaceCheck(str)){
			for(String c : illegalCharacter){
				if(str.indexOf(c) >= 0){
					return false;
				}
			}
		}

		return true;
	}
	

	public static boolean spaceCheck(String str){
		return str != null && !str.trim().isEmpty();
	}
	
	public static boolean nullCheck(String str){
		return str != null;
	}
	
	public static boolean emptyCheck(Collection c){
		return c != null && !c.isEmpty();
	}

	public static boolean emptyCheck(Map m){
		return m != null && !m.isEmpty();
	}
	

	/*
	 * yyyy-mm-dd( hh:mi:ss)?
	 */
	public static boolean dateCheck(String str){
		if( ! spaceCheck(str)){
			return false;
		}
		String reg = "^\\d{4}-\\d{2}-\\d{2}(\\s{1}\\d{2}:\\d{2}:\\d{2})?$";
		return Pattern.matches(reg, str);
	}
	
	private static boolean isMatch(String regex, String orginal) {
		if (! Chk.spaceCheck(orginal)) {
			return false;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(orginal);
		return matcher.matches();
	}


	public static boolean ipCheck(String ip){
		String regex = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";
		return isMatch(regex,ip);


	}

	public static void main(String[] args) {
		System.out.print(ipCheck("259.120.0.1"));
	}



	
}
