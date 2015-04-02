/**
 * NewsControl.java	  V1.0   2014-11-29 下午2:54:23
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
import com.yln.game.service.SourceService;
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
@Controller("sourceControl")
@RequestMapping(value = "source")
public class SourceControl {

	@Autowired
	private SourceService sourceService;

	@RequestMapping(value = "list", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView list() {
		ModelAndView mv = new ModelAndView();
		List<Source> list = sourceService.getAll();
		mv.addObject("list", list);
		mv.addObject("menu", 5);
		mv.addObject("template", "sourceList.html");
		mv.setViewName("frame");
		return mv;
	}

	@RequestMapping(value = { "edit/{id}" }, method = { RequestMethod.GET })
	public ModelAndView edit(@PathVariable long id) {
		ModelAndView mv = new ModelAndView();
		if (id > 0) {
			Source source = sourceService.getById(id);
			mv.addObject("source", source);
			mv.addObject("menu", 5);
			mv.addObject("template", "sourceEdit.html");
			mv.setViewName("frame");
		} else {
			mv.addObject("menu", 5);
			mv.addObject("template", "sourceEdit.html");
			mv.setViewName("frame");
		}
		return mv;
	}

	@RequestMapping(value = { "add" }, method = { RequestMethod.POST })
	public String add(HttpServletRequest request) {
		Source source = uploadSource(request);
		sourceService.save(source);
		return "redirect:/source/list";
	}

	@RequestMapping(value = { "update" }, method = { RequestMethod.POST })
	public String update(HttpServletRequest request) {
		Source source = uploadSource(request);
		sourceService.update(source);
		return "redirect:/source/list";
	}

	@RequestMapping(value = { "delete/{id}" }, method = { RequestMethod.POST, RequestMethod.GET })
	public String delete(HttpServletRequest request, @PathVariable long id) {
		Source source = sourceService.getById(id);
		if (source != null) {
			sourceService.delete(source);
		}
		return "redirect:/source/list";
	}

	private Source uploadSource(HttpServletRequest request) {
		Source source = new Source();
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
						source.setName(value);
					else if ("url".equals(name))
						source.setUrl(value);
					else if ("description".equals(name))
						source.setDescription(value);
					else if ("id".equals(name))
						source.setId(Long.parseLong(value));

				} else {
					source.setLogo(FileUploadUtil.uploadImage(request, item, "source"));
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return source;
	}
}
