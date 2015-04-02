/**
 * SHAUtils.java	  V1.0   2014-6-11 下午9:07:02
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * descrition：
 *
 * @author yaolinnan
 *
 * <p>modify history:</p>
 */
public class SHAUtils {

	 public static String SHA1(String inStr) {
        MessageDigest md = null;
        String outStr = null;
        try {
        	md = MessageDigest.getInstance("SHA-1");
            md.update(inStr.getBytes());
            outStr = bytes2Hex(md.digest());
        }
        catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
        return outStr;
        }
	 
	 private static String bytes2Hex(byte[] bts) {
	        String des = "";
	        String tmp = null;
	        for (int i = 0; i < bts.length; i++) {
	            tmp = (Integer.toHexString(bts[i] & 0xFF));
	            if (tmp.length() == 1) {
	                des += "0";
	            }
	            des += tmp;
	        }
	        return des;
	    }
}
