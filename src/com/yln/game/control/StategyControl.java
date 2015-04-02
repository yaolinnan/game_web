/**
 * StategyControl.java	  V1.0   2014-11-29 下午3:56:50
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.control;

import java.io.UnsupportedEncodingException;
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

import com.yln.game.model.Source;
import com.yln.game.model.Stategy;
import com.yln.game.service.SourceService;
import com.yln.game.service.StategyService;
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
@Controller("stategyControl")
@RequestMapping(value = "stategy")
public class StategyControl {

	@Autowired
	private StategyService stategyService;

	@Autowired
	private SourceService sourceService;

	@RequestMapping(value = "list", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView list(String page, String game) {
		ModelAndView mv = new ModelAndView();
		Page<Stategy> p = new Page<Stategy>();
		if (page == null || "".equals(page)) {
			page = "1";
		}
		p.setCurrentPage(Integer.parseInt(page));
		p.setShowCount(10);
		p.setSortName("id");
		p.setSortOrder("desc");
		p = stategyService.getByPage(p, game);
		mv.addObject("list", p.getResultList());
		mv.addObject("game", game);
		mv.addObject("page", p);
		mv.addObject("menu", 3);
		mv.addObject("template", "stategyList.html");
		mv.setViewName("frame");
		return mv;
	}

	@RequestMapping(value = { "edit/{id}" }, method = { RequestMethod.GET })
	public ModelAndView edit(@PathVariable long id) {
		ModelAndView mv = new ModelAndView();
		List<Source> list = sourceService.getAll();
		if (id > 0) {
			Stategy stategy = stategyService.getById(id);
			mv.addObject("stategy", stategy);
			mv.addObject("sources", list);
			mv.addObject("menu", 3);
			mv.addObject("template", "stategyEdit.html");
			mv.setViewName("frame");
		} else {
			mv.addObject("sources", list);
			mv.addObject("menu", 3);
			mv.addObject("template", "stategyEdit.html");
			mv.setViewName("frame");
		}
		return mv;
	}

	@RequestMapping(value = { "add" }, method = { RequestMethod.POST })
	public String add(HttpServletRequest request) {
		Stategy news = uploadStategy(request);
		stategyService.save(news);
		return "redirect:/stategy/list";
	}

	@RequestMapping(value = { "update" }, method = { RequestMethod.POST })
	public String update(HttpServletRequest request) {
		Stategy news = uploadStategy(request);

		stategyService.update(news);
		return "redirect:/stategy/list";
	}

	@RequestMapping(value = { "delete/{id}" }, method = { RequestMethod.POST, RequestMethod.GET })
	public String delete(HttpServletRequest request, @PathVariable long id) {
		Stategy stategy = stategyService.getById(id);
		if (stategy != null) {
			FileUploadUtil.deleteImage("stategy", stategy.getImage());
			stategyService.delete(stategy);
		}
		return "redirect:/stategy/list";
	}

	private Stategy uploadStategy(HttpServletRequest request) {
		Stategy news = new Stategy();
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
					else if ("game".equals(name))
						news.setGame(value);
					else if ("source".equals(name))
						news.setSource(sourceService.getById(Long.parseLong(value)));
					else if ("id".equals(name))
						news.setId(Long.parseLong(value));

				} else {
					news.setImage(FileUploadUtil.uploadImage(request, item, "stategy"));
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
