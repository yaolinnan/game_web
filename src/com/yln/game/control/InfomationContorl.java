/**
 * InfomationContorl.java	  V1.0   2014-11-29 下午3:12:37
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

import com.yln.game.model.Information;
import com.yln.game.model.Source;
import com.yln.game.service.InformationService;
import com.yln.game.service.SourceService;
import com.yln.game.util.FileUploadUtil;
import com.yln.game.util.Page;

/**
 * descrition：
 * 
 * @author yaolinnan
 * 
 *         <p>
 *         modify history:
 *         </p>
 */
@Controller("infoControl")
@RequestMapping(value = "info")
public class InfomationContorl {

	@Autowired
	private InformationService informationService;

	@Autowired
	private SourceService sourceService;

	@RequestMapping(value = "list", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView list(String page, String start_time, String end_time) {
		ModelAndView mv = new ModelAndView();
		Page<Information> p = new Page<Information>();
		if (page == null || "".equals(page)) {
			page = "1";
		}
		p.setCurrentPage(Integer.parseInt(page));
		p.setShowCount(10);
		p.setSortName("id");
		p.setSortOrder("desc");
		p = informationService.getByPage(p, start_time, end_time);
		mv.addObject("list", p.getResultList());
		mv.addObject("start_time", start_time);
		mv.addObject("end_time", end_time);
		mv.addObject("page", p);
		mv.addObject("menu", 4);
		mv.addObject("template", "infoList.html");
		mv.setViewName("frame");
		return mv;
	}

	@RequestMapping(value = { "edit/{id}" }, method = { RequestMethod.GET })
	public ModelAndView edit(@PathVariable long id) {
		ModelAndView mv = new ModelAndView();
		List<Source> list = sourceService.getAll();
		if (id > 0) {
			Information info = informationService.getById(id);
			mv.addObject("info", info);
			mv.addObject("sources", list);
			mv.addObject("menu", 4);
			mv.addObject("template", "infoEdit.html");
			mv.setViewName("frame");
		} else {
			mv.addObject("sources", list);
			mv.addObject("menu", 4);
			mv.addObject("template", "infoEdit.html");
			mv.setViewName("frame");
		}
		return mv;
	}

	@RequestMapping(value = { "add" }, method = { RequestMethod.POST })
	public String add(HttpServletRequest request) {
		Information news = uploadInfo(request);
		informationService.save(news);
		return "redirect:/info/list";
	}

	@RequestMapping(value = { "update" }, method = { RequestMethod.POST })
	public String update(HttpServletRequest request) {
		Information news = uploadInfo(request);

		informationService.update(news);
		return "redirect:/info/list";
	}

	@RequestMapping(value = { "delete/{id}" }, method = { RequestMethod.POST, RequestMethod.GET })
	public String delete(HttpServletRequest request, @PathVariable long id) {
		Information info = informationService.getById(id);
		if (info != null) {
			FileUploadUtil.deleteImage("info", info.getImage());
			informationService.delete(info);
		}
		return "redirect:/info/list";
	}

	private Information uploadInfo(HttpServletRequest request) {
		Information news = new Information();
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
					if ("title".equals(name))
						news.setTitle(value);
					else if ("content".equals(name))
						news.setContent(value);
					else if ("description".equals(name))
						news.setDescription(value);
					else if ("source".equals(name))
						news.setSource(sourceService.getById(Long.parseLong(value)));
					else if ("type".equals(name))
						news.setType(value);
					else if ("id".equals(name))
						news.setId(Long.parseLong(value));
					else if ("time".equals(name))
						news.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(value));

				} else {
					news.setImage(FileUploadUtil.uploadImage(request, item, "info"));
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return news;
	}
}
