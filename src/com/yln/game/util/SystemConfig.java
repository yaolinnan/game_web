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
import java.util.Properties;

/**
 * descrition：
 *
 * @author yaolinnan
 *
 * <p>modify history:</p>
 */
public class SystemConfig {

	private static SystemConfig config=null;
	
	private static String FILE_NAME="config.properties";
	
	private Properties p=null;

	private SystemConfig(){
		try {
			p=new Properties();
			p.load(this.getClass().getClassLoader().getResourceAsStream(FILE_NAME));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static SystemConfig getInstance(){
		if(config==null){
			config=new SystemConfig();
		}
		return config;
	}
	
	public String getProp(String key){
		
		return p.getProperty(key);
	}
	
}
