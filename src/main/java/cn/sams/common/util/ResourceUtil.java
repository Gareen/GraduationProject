package cn.sams.common.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


public class ResourceUtil {
	
	private static Map<String,Properties> propertiesMap = new ConcurrentHashMap<String, Properties>();
	
	private static ClassLoader cl = ResourceUtil.class.getClassLoader();
	
	private static final String EXTENSION = ".properties";
	

	/**
	 * 该方法获取配置文件
	 * @param fileName	
	 * @param reload 是否重新加载
	 * @return
	 */
	public static Properties getProperties(String fileName, boolean reload){
		
		Properties p = new Properties();
		
		if(! Chk.spaceCheck(fileName)){
			return null;
		}
		
		fileName += ( fileName.endsWith(EXTENSION) ? "" : EXTENSION ); 
			

		/**
		 * 重新加载\更新资源配置缓存
		 */
		if(reload){

			InputStreamReader isr = null;

			try {
				isr = new InputStreamReader(cl.getResourceAsStream(fileName),"UTF-8");
				p.load(isr);
				propertiesMap.put(fileName, p);
			}catch (IOException e) {
				e.printStackTrace();
				p = null;
			}finally{
				if(isr != null){
					try {
						isr.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return p;
			}
		}
		/**
		 * 从缓存中获取
		 * 如果缓存中没有 则重新加载
		 */
		else{
			p = propertiesMap.get(fileName);
			return p == null ? getProperties(fileName, true) : p;
		}		

	}
	
	/**
	 * 该方法获取配置文件,
	 * 	如果缓存中存在这个文件,则直接返回
	 * 	如果不存在 则从系统中重新加载
	 * @param fileName
	 * @return
	 */
	public static Properties getProperties(String fileName){
		return getProperties(fileName, false);
	}
	
	
	/**
	 * 该方法获取配置文件中的属性
	 * @param fileName,key,reload
	 * @return
	 */
	public static String getProperty(String fileName, String key, boolean reload){
		Properties p = getProperties(fileName, reload);
		
		if(p == null){
			return null;
		}
		return p.getProperty(key);
	}
	
	
	/**
	 * 该方法获取配置文件中的属性
	 * @param fileName,key
	 * @return
	 */
	public static String getProperty(String fileName, String key){
		return getProperty(fileName, key, false);
	}


	/**
	 * 从args文件中获取属性
	 * @param key
	 * @return
     */
	public static String getProperty(String key){
		return getProperty("args", key, false);
	}


	/**
	 * 	 *从args文件中获取属性(可选是否重新加载)
	 * @param key
	 * @param reload
     * @return
     */
	public static String getProperty(String key,boolean reload){
		return getProperty("args", key, reload);
	}
	
	
	
}
