/**
 * WebSiteControl.java	  V1.0   2014-8-8 下午5:16:40
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.control;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yln.game.model.Ad;
import com.yln.game.model.Information;
import com.yln.game.model.News;
import com.yln.game.model.Stategy;
import com.yln.game.service.AdService;
import com.yln.game.service.InformationService;
import com.yln.game.service.NewsService;
import com.yln.game.service.StategyService;
import com.yln.game.util.Constans;
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
@Controller("webSiteControl")
@RequestMapping(value = "website")
public class WebSiteControl {

	@Autowired
	private AdService adService;

	@Autowired
	private NewsService newsService;

	@Autowired
	private InformationService informationService;

	@Autowired
	private StategyService stategyService;

	@RequestMapping(value = "index", method = { RequestMethod.GET })
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView();
		List<Ad> ads = adService.queryAll(Constans.AD_NORMAL);
		mv.addObject("ads", ads);
		mv.setViewName("weixin/index");
		return mv;
	}

	@RequestMapping(value = "about", method = { RequestMethod.GET })
	public ModelAndView about() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("weixin/about");
		return mv;
	}

	@RequestMapping(value = "news", method = { RequestMethod.GET })
	public ModelAndView news(String page) {
		ModelAndView mv = new ModelAndView();
		Page<News> p = new Page<News>();
		int currentPage = 0;
		if (StringUtils.isEmpty(page)) {
			currentPage = 1;
		} else {
			currentPage = Integer.parseInt(page);
		}
		p.setCurrentPage(currentPage);
		p.setShowCount(20);
		p.setSortName("id");
		p.setSortOrder("desc");
		p = newsService.getByPage(p, null, null);
		mv.addObject("list", p.getResultList());
		mv.addObject("currentPage", p.getCurrentPage());
		mv.addObject("totalPage", p.getTotalPage());
		mv.setViewName("weixin/news");
		return mv;
	}

	@RequestMapping(value = "news/{id}", method = { RequestMethod.GET })
	public ModelAndView newsDetail(@PathVariable long id) {
		ModelAndView mv = new ModelAndView();
		News history = newsService.getById(id);
		mv.addObject("news", history);
		mv.setViewName("weixin/news_detail");
		return mv;
	}

	@RequestMapping(value = "info", method = { RequestMethod.GET })
	public ModelAndView info(String page) {
		ModelAndView mv = new ModelAndView();
		Page<Information> p = new Page<Information>();
		int currentPage = 0;
		if (StringUtils.isEmpty(page)) {
			currentPage = 1;
		} else {
			currentPage = Integer.parseInt(page);
		}
		p.setCurrentPage(currentPage);
		p.setShowCount(20);
		p.setSortName("id");
		p.setSortOrder("desc");
		p = informationService.getByPage(p, null, null);
		mv.addObject("list", p.getResultList());
		mv.addObject("currentPage", p.getCurrentPage());
		mv.addObject("totalPage", p.getTotalPage());
		mv.setViewName("weixin/info");
		return mv;
	}

	@RequestMapping(value = "info/{id}", method = { RequestMethod.GET })
	public ModelAndView infoDetail(@PathVariable long id) {
		ModelAndView mv = new ModelAndView();
		Information info = informationService.getById(id);
		mv.addObject("info", info);
		mv.setViewName("weixin/info_detail");
		return mv;
	}

	@RequestMapping(value = "stategy", method = { RequestMethod.GET })
	public ModelAndView stategy(String page) {
		ModelAndView mv = new ModelAndView();
		Page<Stategy> p = new Page<Stategy>();
		int currentPage = 0;
		if (StringUtils.isEmpty(page)) {
			currentPage = 1;
		} else {
			currentPage = Integer.parseInt(page);
		}
		p.setCurrentPage(currentPage);
		p.setShowCount(20);
		p.setSortName("id");
		p.setSortOrder("desc");
		p = stategyService.getByPage(p, null);
		mv.addObject("list", p.getResultList());
		mv.addObject("currentPage", p.getCurrentPage());
		mv.addObject("totalPage", p.getTotalPage());
		mv.setViewName("weixin/stategy");
		return mv;
	}

	@RequestMapping(value = "stategy/{id}", method = { RequestMethod.GET })
	public ModelAndView stategyDetail(@PathVariable long id) {
		ModelAndView mv = new ModelAndView();
		Stategy info = stategyService.getById(id);
		mv.addObject("stategy", info);
		mv.setViewName("weixin/stategy_detail");
		return mv;
	}
}
