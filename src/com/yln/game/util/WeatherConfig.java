/**
 * SystemConfig.java	  V1.0   2014-6-11 下午8:47:53
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * descrition：
 *
 * @author yaolinnan
 *
 * <p>modify history:</p>
 */
public class WeatherConfig {

	private static WeatherConfig config=null;
	
	private static String FILE_NAME="weather.properties";
	
	private Properties p=null;
	
	private static Map<String,String> map=new HashMap<String,String>();

	private WeatherConfig(){
		try {
			p=new Properties();
			p.load(this.getClass().getClassLoader().getResourceAsStream(FILE_NAME));
			Enumeration<?> e=p.propertyNames();
			while(e.hasMoreElements()){
				String key=(String) e.nextElement();
				String value=p.getProperty(key);
				map.put(value, key);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static WeatherConfig getInstance(){
		if(config==null){
			config=new WeatherConfig();
		}
		return config;
	}
	
	
	
	public String getCityCode(String city){
		
		return map.get(city);
	}
	
}
