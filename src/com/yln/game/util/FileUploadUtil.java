/**
 * FileUploadUtil.java	  V1.0   2014-5-17 下午3:31:34
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;

/**
 * descrition：
 * 
 * @author yaolinnan
 * 
 *         <p>
 *         modify history:
 *         </p>
 */
public class FileUploadUtil {

	public static String uploadImage(HttpServletRequest request, FileItem item, String dirName)
			throws Exception {
		SystemConfig config = SystemConfig.getInstance();
		String strDirPath = config.getProp("upload.image");
		// String strDirPath = request.getSession().getServletContext().getRealPath("/");
		strDirPath += File.separator + dirName;
		String path = "";
		File dir = new File(strDirPath);
		if (!dir.exists()) {
			dir.mkdir();
		}
		Date now = new Date();
		String fName = new SimpleDateFormat("yyyyMMdd").format(now) + item.getName();
		if (!"".equals(fName)
				&& (fName.endsWith("jpg") || fName.endsWith("png") || fName.endsWith("jpeg"))) {
			String sFilePath = strDirPath + File.separator + fName;
			File file = new File(sFilePath);
			item.write(file);
			path = config.getProp("virtual.path") + File.separator + dirName + File.separator
					+ file.getName();
		}
		return path;
	}

	private static void delete(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	public static void deleteImage(String file, String image) {
		if (!StringUtils.isEmpty(image)) {
			if (image.contains(File.separator)) {
				int index = image.lastIndexOf(File.separator);
				String name = image.substring(index + 1);
				SystemConfig config = SystemConfig.getInstance();
				String path = config.getProp("upload.image") + File.separator + file
						+ File.separator + name;
				delete(path);
			}
		}
	}
}
