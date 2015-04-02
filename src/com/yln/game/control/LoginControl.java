/**
 * LoginControl.java	  V1.0   2014-5-10 下午3:06:48
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.control;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yln.game.model.User;
import com.yln.game.service.UserService;
import com.yln.game.util.Constans;
import com.yln.game.util.GetRandomCodeUtil;
import com.yln.game.util.MD5Util;

/**
 * descrition：
 * 
 * @author yaolinnan
 * 
 *         <p>
 *         modify history:
 *         </p>
 */
@Controller("loginControl")
public class LoginControl {

	@Autowired
	private UserService userService;

	@RequestMapping(value = { "login" }, method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest req, HttpServletResponse res) throws IOException {
		ModelAndView mv = new ModelAndView("login");
		return mv;
	}

	@RequestMapping(value = { "loginOut" }, method = RequestMethod.GET)
	public ModelAndView loginout(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		// res.sendRedirect("login.html");
		req.getSession().removeAttribute("user");
		ModelAndView mv = new ModelAndView("login");
		return mv;
	}

	@RequestMapping(value = { "/doLogin" }, method = RequestMethod.POST)
	@ResponseBody
	public String doLogin(User user, String code, HttpServletRequest req, HttpServletResponse res)
			throws IOException, NoSuchAlgorithmException {
		String c = (String) req.getSession().getAttribute("code");
		if (!(c.toLowerCase()).equals((code.toLowerCase()))) {
			return "-1";
		}
		User u = userService.login(user.getName(), MD5Util.getMD5DigestHex(user.getPassword()),
				Constans.USER_ROLE_MANAGER);
		if (u != null) {
			if ("1".equals(u.getStatus())) {
				req.getSession().setAttribute("user", u);
				return "0";
			} else {
				return "2";
			}
		} else {
			return "1";
		}
	}

	@RequestMapping(value = { "/code" }, method = RequestMethod.GET)
	public void code(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String imageCode = GetRandomCodeUtil.GetCode(4);
		byte[] b = GetRandomCodeUtil.getImage(imageCode);
		req.getSession().setAttribute("code", imageCode);
		req.getSession().setMaxInactiveInterval(60000);
		res.setContentType("image/jpeg");
		res.setBufferSize(2048);
		res.getOutputStream().write(b);
	}
}
