/**
 * MessageControl.java	  V1.0   2014-12-11 下午2:46:05
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yln.game.model.Message;
import com.yln.game.model.User;
import com.yln.game.service.MessageService;
import com.yln.game.service.UserService;
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
@Controller("messageControl")
@RequestMapping(value = "message")
public class MessageControl {

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "list", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView list(String page, String status) {
		ModelAndView mv = new ModelAndView();
		Page<Message> p = new Page<Message>();
		if (page == null || "".equals(page)) {
			page = "1";
		}
		if (status == null || "".equals(status)) {
			status = "0";
		}
		p.setCurrentPage(Integer.parseInt(page));
		p.setShowCount(10);
		p.setSortName("id");
		p.setSortOrder("desc");
		p = messageService.getByPage(p, status);
		mv.addObject("list", p.getResultList());
		mv.addObject("status", status);
		mv.addObject("page", p);
		mv.addObject("menu", 9);
		mv.addObject("template", "messageList.html");
		mv.setViewName("frame");
		return mv;
	}

	@RequestMapping(value = "edit", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView edit() {
		ModelAndView mv = new ModelAndView();
		List<User> list = userService.getByRole("1");
		mv.addObject("list", list);
		mv.addObject("menu", 9);
		mv.addObject("template", "messageEdit.html");
		mv.setViewName("frame");
		return mv;
	}

	@RequestMapping(value = "add", method = { RequestMethod.GET, RequestMethod.POST })
	public String save(HttpServletRequest request) {
		Message msg = new Message();
		String content = request.getParameter("content");
		String type = request.getParameter("type");
		String userId = request.getParameter("userId");
		if (!StringUtils.isEmpty(content) && !StringUtils.isEmpty(userId)) {
			msg.setContent(content);
			msg.setType(type);
			msg.setStatus("0");
			long id = Long.parseLong(userId);
			if (id > 0) {
				User user = userService.getById(id);
				msg.setUser(user);
				messageService.save(msg);
			} else {
				List<User> list = userService.getByRole("1");// 查询所有普通用户
				if (list != null && list.size() > 0) {
					for (User user : list) {
						msg.setUser(user);
						messageService.save(msg);
					}
				}
			}
		}
		return "redirect:/message/list";
	}

	@RequestMapping(value = "delete/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	public String delete(@PathVariable long id) {
		Message msg = messageService.getById(id);
		if (msg != null) {
			messageService.delete(msg);
		}
		return "redirect:/message/list";
	}

}
