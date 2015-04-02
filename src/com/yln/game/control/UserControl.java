/**
 * UserControl.java	  V1.0   2014-5-13 下午9:05:35
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.control;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yln.game.model.User;
import com.yln.game.service.UserService;
import com.yln.game.util.MD5Util;
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
@Controller("userControl")
@RequestMapping(value = "user")
public class UserControl {

	@Autowired
	private UserService userService;

	@RequestMapping(value = { "/list" }, method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView list(String page, String name) {
		ModelAndView mv = new ModelAndView();
		Page<User> p = new Page<User>();
		if (page == null || "".equals(page)) {
			page = "1";
		}
		p.setCurrentPage(Integer.parseInt(page));
		p.setShowCount(10);
		p.setSortName("id");
		p.setSortOrder("desc");
		p = userService.getByPage(p, name);
		mv.addObject("list", p.getResultList());
		mv.addObject("name", name);
		mv.addObject("page", p);
		mv.addObject("menu", 1);
		mv.addObject("template", "userList.html");
		mv.setViewName("frame");
		return mv;
	}

	@RequestMapping(value = { "/edit" }, method = { RequestMethod.GET })
	public ModelAndView edit() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("menu", 1);
		mv.addObject("template", "userEdit.html");
		mv.setViewName("frame");
		return mv;
	}

	@RequestMapping(value = { "/add" }, method = { RequestMethod.POST })
	@ResponseBody
	public String add(String name, String password, String email, String telephone)
			throws NoSuchAlgorithmException {
		User u = userService.getByName(name);
		if (u != null)
			return "2";
		User user = new User();
		user.setName(name);
		user.setPassword(MD5Util.getMD5DigestHex(password));
		user.setEmail(email);
		user.setTelephone(telephone);
		user.setCreateTime(new Date());
		user.setStatus("1");
		user.setRole("0");
		user.setIntegral(0);
		userService.save(user);
		return "1";
	}

	@RequestMapping(value = { "/updateStatus/{userId}/{status}" }, method = { RequestMethod.GET })
	public String updateStatus(@PathVariable long userId, @PathVariable String status) {
		User user = userService.getById(userId);
		user.setStatus(status);
		userService.update(user);
		return "redirect:/user/list";
	}

	@RequestMapping(value = { "/updatePwd" }, method = { RequestMethod.POST })
	@ResponseBody
	public String updatePwd(long id, String password) throws NoSuchAlgorithmException {
		if (password == null || "".equals(password))
			return "0";
		User user = userService.getById(id);
		user.setPassword(MD5Util.getMD5DigestHex(password));
		// user.setName(name);
		userService.update(user);
		return "1";
	}

}
