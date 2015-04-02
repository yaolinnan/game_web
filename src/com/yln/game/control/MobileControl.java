/**
 * MobileControl.java	  V1.0   2014-7-15 下午9:57:47
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.control;

import java.io.IOException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.yln.game.model.Ad;
import com.yln.game.model.Feedback;
import com.yln.game.model.InfoComment;
import com.yln.game.model.Information;
import com.yln.game.model.Message;
import com.yln.game.model.News;
import com.yln.game.model.NewsComment;
import com.yln.game.model.Stategy;
import com.yln.game.model.User;
import com.yln.game.model.vo.BasicData;
import com.yln.game.model.vo.CollectVO;
import com.yln.game.model.vo.InfoCommentVO;
import com.yln.game.model.vo.InfoVO;
import com.yln.game.model.vo.NewsCommentVO;
import com.yln.game.model.vo.NewsVO;
import com.yln.game.model.vo.ReturnVO;
import com.yln.game.model.vo.StategyVO;
import com.yln.game.model.vo.UserVO;
import com.yln.game.service.AdService;
import com.yln.game.service.FeedbackService;
import com.yln.game.service.InfoCommentService;
import com.yln.game.service.InformationService;
import com.yln.game.service.MessageService;
import com.yln.game.service.NewsCommentService;
import com.yln.game.service.NewsService;
import com.yln.game.service.StategyService;
import com.yln.game.service.UserService;
import com.yln.game.util.Constans;
import com.yln.game.util.MD5Util;
import com.yln.game.util.Page;
import com.yln.game.util.SHAUtils;
import com.yln.game.util.SystemConfig;

/**
 * descrition：
 * 
 * @author yaolinnan
 * 
 *         <p>
 *         modify history:
 *         </p>
 */
@Controller("mobileControl")
@RequestMapping(value = "mobile")
public class MobileControl {

	@Autowired
	private AdService adService;

	@Autowired
	private UserService userService;

	@Autowired
	private FeedbackService feedbackService;

	@Autowired
	private NewsService newsService;

	@Autowired
	private StategyService stategyService;

	@Autowired
	private InformationService informationService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private NewsCommentService newsCommentService;

	@Autowired
	private InfoCommentService infoCommentService;

	@RequestMapping(value = "index", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String getBasicData(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String TOKEN = SHAUtils.SHA1(SystemConfig.getInstance().getProp("client.token"));
		// String token = request.getParameter("token");
		String token = request.getHeader("token");
		String user = request.getHeader("user");
		if (TOKEN.equals(token)) {
			Page<News> page = new Page<News>();
			page.setCurrentPage(1);
			page.setShowCount(20);
			page.setSortName("id");
			page.setSortOrder("desc");
			List<Ad> ads = adService.queryAll(0);
			List<News> news = newsService.getByPage(page, null, null).getResultList();
			Page<Information> p = new Page<Information>();
			p.setShowCount(20);
			p.setCurrentPage(1);
			p.setSortName("id");
			p.setSortOrder("desc");
			p = informationService.getByPage(p, null, null);
			Page<Stategy> pp = new Page<Stategy>();
			pp.setShowCount(20);
			pp.setCurrentPage(1);
			pp.setSortName("id");
			pp.setSortOrder("desc");
			pp = stategyService.getByPage(pp, null);
			BasicData data = new BasicData();
			data.setAds(ads);
			data.setInfos(p.getResultList());
			data.setNews(news);
			data.setStategys(pp.getResultList());
			if (!StringUtils.isEmpty(user)) {
				List<Message> messages = messageService.getByUser(Long.parseLong(user), "0");
				data.setMessages(messages);
			}
			send(data, response);
		}
		return null;
	}

	@RequestMapping(value = "login", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String login(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String TOKEN = SHAUtils.SHA1(SystemConfig.getInstance().getProp("client.token"));
		request.setCharacterEncoding("utf-8");
		String token = request.getHeader("token");
		if (TOKEN.equals(token)) {
			String n = request.getHeader("name");
			String name = URLDecoder.decode(n, "utf-8");
			String password = request.getHeader("password");
			UserVO vo = new UserVO();
			try {
				User user = userService.login(name, MD5Util.getMD5DigestHex(password),
						Constans.USER_ROLE_NORMAL);
				if (user != null) {
					vo.setCode(Constans.RETURN_CODE_SUCCESS);
					vo.setMsg(name + "登录成功");
					vo.setId(user.getId());
					vo.setName(user.getName());
					vo.setEmail(user.getEmail());
					vo.setTelephone(user.getTelephone());
					// vo.setScore(user.getIntegral());
				} else {
					vo.setCode(Constans.RETURN_CODE_FAILTRUE);
					vo.setMsg(name + "登录失败");
				}
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			send(vo, response);
		}
		return null;
	}

	@RequestMapping(value = "register", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String register(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String TOKEN = SHAUtils.SHA1(SystemConfig.getInstance().getProp("client.token"));
		request.setCharacterEncoding("utf-8");
		String token = request.getHeader("token");
		ReturnVO vo = new ReturnVO();
		if (TOKEN.equals(token)) {
			String n = request.getHeader("name");
			String name = URLDecoder.decode(n, "utf-8");
			User u = userService.getByName(name);
			if (u != null) {
				vo.setCode(Constans.RETURN_CODE_FAILTRUE);
				vo.setMsg(name + "已经存在，无法注册");
			} else {
				String password = request.getHeader("password");
				String email = request.getHeader("email");
				String telephone = request.getHeader("telephone");
				User user = new User();
				user.setEmail(email);
				user.setName(name);
				try {
					user.setPassword(MD5Util.getMD5DigestHex(password));
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
				user.setRole(Constans.USER_ROLE_NORMAL);
				user.setTelephone(telephone);
				user.setStatus("1");
				long id = userService.save(user);
				if (id > 0) {
					vo.setCode((int) id);
					vo.setMsg(name + "注册成功");
				} else {
					vo.setCode(Constans.RETURN_CODE_FAILTRUE);
					vo.setMsg(name + "注册失败");
				}
			}
			send(vo, response);
		}
		return null;
	}

	@RequestMapping(value = "updateUser", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String updateUser(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String TOKEN = SHAUtils.SHA1(SystemConfig.getInstance().getProp("client.token"));
		String token = request.getHeader("token");
		ReturnVO vo = new ReturnVO();
		if (TOKEN.equals(token)) {
			String id = request.getHeader("id");
			User u = userService.getById(Long.parseLong(id));
			if (u != null) {

				String password = request.getHeader("password");
				String email = request.getHeader("email");
				String telephone = request.getHeader("telephone");
				User user = new User();
				user.setEmail(email);
				try {
					user.setPassword(MD5Util.getMD5DigestHex(password));
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
				user.setTelephone(telephone);
				userService.update(user);
				vo.setCode(Constans.RETURN_CODE_SUCCESS);
				vo.setMsg("修改资料成功");
			} else {
				vo.setCode(Constans.RETURN_CODE_FAILTRUE);
				vo.setMsg("修改资料失败");
			}
			send(vo, response);
		}
		return null;
	}

	@RequestMapping(value = "news", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String getNews(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String TOKEN = SHAUtils.SHA1(SystemConfig.getInstance().getProp("client.token"));
		String token = request.getHeader("token");
		if (TOKEN.equals(token)) {
			Page<News> p = new Page<News>();
			String page = request.getHeader("page");
			p.setShowCount(20);
			p.setCurrentPage(Integer.parseInt(page));
			p.setSortName("id");
			p.setSortOrder("desc");
			p = newsService.getByPage(p, null, null);
			NewsVO vo = new NewsVO();
			vo.setCurrentPage(p.getCurrentPage());
			vo.setTotalPage(p.getTotalPage());
			vo.setNews(p.getResultList());
			send(vo, response);
		}
		return null;
	}

	@RequestMapping(value = "info", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String getInfo(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String TOKEN = SHAUtils.SHA1(SystemConfig.getInstance().getProp("client.token"));
		String token = request.getHeader("token");
		if (TOKEN.equals(token)) {
			Page<Information> p = new Page<Information>();
			String page = request.getHeader("page");
			p.setShowCount(20);
			p.setCurrentPage(Integer.parseInt(page));
			p.setSortName("id");
			p.setSortOrder("desc");
			p = informationService.getByPage(p, null, null);
			InfoVO vo = new InfoVO();
			vo.setCurrentPage(p.getCurrentPage());
			vo.setTotalPage(p.getTotalPage());
			vo.setList(p.getResultList());
			send(vo, response);
		}
		return null;
	}

	@RequestMapping(value = "stategy", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String getStategy(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String TOKEN = SHAUtils.SHA1(SystemConfig.getInstance().getProp("client.token"));
		String token = request.getHeader("token");
		if (TOKEN.equals(token)) {
			Page<Stategy> p = new Page<Stategy>();
			String page = request.getHeader("page");
			p.setShowCount(20);
			p.setCurrentPage(Integer.parseInt(page));
			p.setSortName("id");
			p.setSortOrder("desc");
			p = stategyService.getByPage(p, null);
			StategyVO vo = new StategyVO();
			vo.setCurrentPage(p.getCurrentPage());
			vo.setTotalPage(p.getTotalPage());
			vo.setList(p.getResultList());
			send(vo, response);
		}
		return null;
	}

	@RequestMapping(value = "sendMessage", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String sendMessage(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String TOKEN = SHAUtils.SHA1(SystemConfig.getInstance().getProp("client.token"));
		request.setCharacterEncoding("utf-8");
		String token = request.getHeader("token");
		if (TOKEN.equals(token)) {
			String m = request.getHeader("message");
			String message = URLDecoder.decode(m, "utf-8");
			String user = request.getHeader("user");
			User u = userService.getById(Long.parseLong(user));
			Feedback msg = new Feedback();
			msg.setMsg(message);
			msg.setUser(u);
			msg.setStatus("0");
			long id = feedbackService.save(msg);
			ReturnVO vo = new ReturnVO();
			if (id > 0) {
				vo.setCode(Constans.RETURN_CODE_SUCCESS);
				vo.setMsg("信息提交成功，感谢你的支持，我们会尽快处理！");
			} else {
				vo.setCode(Constans.RETURN_CODE_FAILTRUE);
				vo.setMsg("信息提交失败");
			}
			send(vo, response);
		}
		return null;
	}

	@RequestMapping(value = "myCollect", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String getCollect(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String TOKEN = SHAUtils.SHA1(SystemConfig.getInstance().getProp("client.token"));
		String token = request.getHeader("token");
		if (TOKEN.equals(token)) {
			String id = request.getHeader("user");
			User user = userService.getById(Long.parseLong(id));
			List<News> news = user.getNews();
			List<Information> infos = user.getInfos();
			List<Stategy> stategys = user.getStategys();
			CollectVO vo = new CollectVO();
			vo.setInfoes(infos);
			vo.setNews(news);
			vo.setStategys(stategys);
			send(vo, response);
		}
		return null;
	}

	@RequestMapping(value = "getComment", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String getComment(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String TOKEN = SHAUtils.SHA1(SystemConfig.getInstance().getProp("client.token"));
		String token = request.getHeader("token");
		if (TOKEN.equals(token)) {
			String id = request.getHeader("id");
			String type = request.getHeader("type");
			if ("1".equals(type)) {
				List<NewsComment> list = newsCommentService.getByComment(Long.parseLong(id));
				NewsCommentVO vo = new NewsCommentVO();
				vo.setList(list);
				vo.setMsg("获取评论成功");
				vo.setSize(list.size());
				send(vo, response);
			} else if ("2".equals(type)) {
				List<InfoComment> list = infoCommentService.getByComment(Long.parseLong(id));
				InfoCommentVO vo = new InfoCommentVO();
				vo.setList(list);
				vo.setMsg("获取评论成功");
				vo.setSize(list.size());
				send(vo, response);
			} else {

			}
		}

		return null;
	}

	@RequestMapping(value = "sendComment", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String sendComment(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String TOKEN = SHAUtils.SHA1(SystemConfig.getInstance().getProp("client.token"));
		String token = request.getHeader("token");
		if (TOKEN.equals(token)) {
			String id = request.getHeader("id");
			String user = request.getHeader("user");
			String type = request.getHeader("type");
			String c = request.getHeader("comment");
			String comment = URLDecoder.decode(c, "utf-8");
			long add = 0;
			if ("1".equals(type)) {
				NewsComment nc = new NewsComment();
				nc.setComment(comment);
				nc.setNews(newsService.getById(Long.parseLong(id)));
				nc.setUser(userService.getById(Long.parseLong(user)));
				nc.setStatus("1");
				add = newsCommentService.add(nc);
			} else if ("2".equals(type)) {
				InfoComment ic = new InfoComment();
				ic.setComment(comment);
				ic.setInfo(informationService.getById(Long.parseLong(id)));
				ic.setUser(userService.getById(Long.parseLong(user)));
				ic.setStatus("1");
				add = infoCommentService.add(ic);
			}
			ReturnVO vo = new ReturnVO();
			if (add > 0) {
				vo.setCode(Constans.RETURN_CODE_SUCCESS);
				vo.setMsg("评论成功");
			} else {
				vo.setCode(Constans.RETURN_CODE_FAILTRUE);
				vo.setMsg("评论失败");
			}
			send(vo, response);
		}

		return null;
	}

	// @RequestMapping(value = "getInfoComment", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	// public String getInfoComment(HttpServletRequest request, HttpServletResponse response)
	// throws IOException {
	// String TOKEN = SHAUtils.SHA1(SystemConfig.getInstance().getProp("client.token"));
	// String token = request.getHeader("token");
	// if (TOKEN.equals(token)) {
	// String info = request.getHeader("info");
	// List<InfoComment> list = infoCommentService.getByComment(Long.parseLong(info));
	// InfoCommentVO vo = new InfoCommentVO();
	// vo.setList(list);
	// vo.setMsg("获取评论成功");
	// vo.setSize(list.size());
	// send(vo, response);
	// }
	//
	// return null;
	// }
	//
	// @RequestMapping(value = "sendInfoComment", method = { RequestMethod.GET, RequestMethod.POST
	// })
	// @ResponseBody
	// public String sendInfoComment(HttpServletRequest request, HttpServletResponse response)
	// throws IOException {
	// String TOKEN = SHAUtils.SHA1(SystemConfig.getInstance().getProp("client.token"));
	// String token = request.getHeader("token");
	// if (TOKEN.equals(token)) {
	// String info = request.getHeader("info");
	// String user = request.getHeader("user");
	// String c = request.getHeader("comment");
	// String comment = URLDecoder.decode(c, "utf-8");
	// InfoComment ic = new InfoComment();
	// ic.setComment(comment);
	// ic.setInfo(informationService.getById(Long.parseLong(info)));
	// ic.setUser(userService.getById(Long.parseLong(user)));
	// long add = infoCommentService.add(ic);
	// ReturnVO vo = new ReturnVO();
	// if (add > 0) {
	// vo.setCode(Constans.RETURN_CODE_SUCCESS);
	// vo.setMsg("评论成功");
	// } else {
	// vo.setCode(Constans.RETURN_CODE_FAILTRUE);
	// vo.setMsg("评论失败");
	// }
	// send(vo, response);
	// }
	//
	// return null;
	// }

	// @RequestMapping(value = "myCollect", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	// public String getCollect(HttpServletRequest request, HttpServletResponse response)
	// throws IOException {
	// String TOKEN = SHAUtils.SHA1(SystemConfig.getInstance().getProp("client.token"));
	// String token = request.getHeader("token");
	// if (TOKEN.equals(token)) {
	// String id = request.getHeader("user");
	// User user = userService.getById(Long.parseLong(id));
	// List<History> historys = user.getHistorys();
	// List<Joker> jokers = user.getJokers();
	// List<Knowledge> knowledges = user.getKnowledges();
	// CollectVO vo = new CollectVO();
	// vo.setHistorys(historys);
	// vo.setJokers(jokers);
	// vo.setKnowledges(knowledges);
	// send(vo, response);
	// }
	// return null;
	// }

	@RequestMapping(value = "collect", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String collect(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String TOKEN = SHAUtils.SHA1(SystemConfig.getInstance().getProp("client.token"));
		String token = request.getHeader("token");
		if (TOKEN.equals(token)) {
			boolean update = false;
			String id = request.getHeader("id");
			String useId = request.getHeader("user");
			String type = request.getHeader("type");
			User user = userService.getById(Long.parseLong(useId));
			if ("1".equals(type)) {
				News news = newsService.getById(Long.parseLong(id));
				List<News> list = user.getNews();
				if (!list.contains(news)) {
					update = true;
					user.getNews().add(news);
				}
			} else if ("2".equals(type)) {
				Information info = informationService.getById(Long.parseLong(id));
				List<Information> list = user.getInfos();
				if (!list.contains(info)) {
					update = true;
					user.getInfos().add(info);
				}
			} else if ("3".equals(type)) {
				Stategy stategy = stategyService.getById(Long.parseLong(id));
				List<Stategy> list = user.getStategys();
				if (!list.contains(stategy)) {
					update = true;
					user.getStategys().add(stategy);
				}
			}
			ReturnVO vo = new ReturnVO();
			if (update) {
				userService.update(user);
				vo.setCode(Constans.RETURN_CODE_SUCCESS);
				vo.setMsg("收藏成功");
			} else {
				vo.setCode(Constans.RETURN_CODE_SUCCESS);
				vo.setMsg("已经收藏过，无法重复收藏");
			}
			send(vo, response);
		}
		return null;
	}

	private void send(Object o, HttpServletResponse response) throws IOException {
		Gson gson = new Gson();
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		response.getWriter().write(gson.toJson(o));
	}
}
