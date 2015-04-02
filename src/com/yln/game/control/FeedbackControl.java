/**
 * MessageControl.java	  V1.0   2014-5-20 下午8:42:52
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yln.game.model.Feedback;
import com.yln.game.service.FeedbackService;
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
@Controller("feedbackControl")
@RequestMapping(value = "feedback")
public class FeedbackControl {

	@Autowired
	private FeedbackService feedbackService;

	@RequestMapping(value = "list", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView list(String page, String time) {
		ModelAndView mv = new ModelAndView();
		Page<Feedback> p = new Page<Feedback>();
		if (page == null || "".equals(page)) {
			page = "1";
		}
		p.setCurrentPage(Integer.parseInt(page));
		p.setShowCount(10);
		p.setSortName("id");
		p.setSortOrder("desc");
		p = feedbackService.getByPage(p, time);
		mv.addObject("list", p.getResultList());
		mv.addObject("time", time);
		mv.addObject("page", p);
		mv.addObject("menu", 6);
		mv.addObject("template", "feedbackList.html");
		mv.setViewName("frame");
		return mv;
	}

	@RequestMapping(value = "update/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateStatus(@PathVariable long id) {
		Feedback message = feedbackService.getById(id);
		message.setStatus("1");
		feedbackService.update(message);
		return "redirect:/message/list";
	}
}
