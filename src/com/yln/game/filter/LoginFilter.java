/**
 * LoginFilter.java	  V1.0   2013-5-27 上午10:44:14
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yln.game.model.User;

public class LoginFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		req.getSession().getServletContext().getRealPath("/WEB-INF");
		User user = (User) req.getSession().getAttribute("user");
		if (user == null) {
			res.sendRedirect(req.getContextPath() + "/login");
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
	}

}
