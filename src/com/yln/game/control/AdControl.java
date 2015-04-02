/**
 * AdControl.java	  V1.0   2014-7-15 下午9:15:02
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.control;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yln.game.model.Ad;
import com.yln.game.service.AdService;
import com.yln.game.util.FileUploadUtil;

/**
 * descrition：
 * 
 * @author yaolinnan
 * 
 *         <p>
 *         modify history:
 *         </p>
 */
@Controller("adControl")
@RequestMapping(value = "ad")
public class AdControl {

	@Autowired
	private AdService adService;

	@RequestMapping(value = "list", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView list() {
		ModelAndView mv = new ModelAndView();
		List<Ad> list = adService.queryAll(0);
		mv.addObject("list", list);
		mv.addObject("menu", 7);
		mv.addObject("template", "adList.html");
		mv.setViewName("frame");
		return mv;
	}

	@RequestMapping(value = "edit", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView edit() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("menu", 7);
		mv.addObject("template", "adEdit.html");
		mv.setViewName("frame");
		return mv;
	}

	@RequestMapping(value = { "add" }, method = { RequestMethod.POST })
	public String add(HttpServletRequest request) {
		Ad ad = uploadAd(request);
		adService.save(ad);
		return "redirect:/ad/list";
	}

	@RequestMapping(value = { "delete/{id}" }, method = { RequestMethod.POST, RequestMethod.GET })
	public String delete(@PathVariable long id) {
		adService.delete(id);
		return "redirect:/ad/list";
	}

	private Ad uploadAd(HttpServletRequest request) {
		Ad ad = new Ad();
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List<FileItem> items = upload.parseRequest(request);
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = item.getString("UTF-8");
					if ("name".equals(name))
						ad.setName(value);
					else if ("type".equals(name))
						ad.setType(Integer.parseInt(value));
					else if ("url".equals(name))
						ad.setUrl(value);

					else if ("time".equals(name))
						ad.setValidTime(new SimpleDateFormat("yyyy-MM-dd").parse(value));

				} else {
					ad.setPath(FileUploadUtil.uploadImage(request, item, "ad"));
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ad;
	}
}
