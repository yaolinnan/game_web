/**
 * WeixinController.java	  V1.0   2014-6-11 下午8:51:46
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.yln.game.util.SHAUtils;
import com.yln.game.util.SystemConfig;
import com.yln.game.util.WeatherConfig;
import com.yln.game.vo.Weather;

/**
 * descrition：
 * 
 * @author yaolinnan
 * 
 *         <p>
 *         modify history:
 *         </p>
 */
@Controller("weixin")
public class WeixinControl {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String[] stars = { "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座",
			"射手座", "摩羯座", "水瓶座", "双鱼座" };

	@RequestMapping(value = "weixin/test", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String test(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String phone = "18670042487";
		String url = SystemConfig.getInstance().getProp("phone.url").replaceAll("<phone>", phone);
		String content = "";
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse res = client.execute(get);
			InputStream in = res.getEntity().getContent();
			SAXReader reader = new SAXReader();
			Document doc = reader.read(in);
			Element root = doc.getRootElement();
			Element result = root.element("result");
			if ("1".equals(root.elementText("success"))) {
				String type = result.elementText("ctype");
				String address = result.elementText("att");
				content = phone + ":" + address + " " + type;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		response.setContentType("text/html;charset=UTF-8");
		// response.setCharacterEncoding("utf-8");
		response.getWriter().write(content);
		return null;
	}

	@RequestMapping(value = "weixin/index", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String weixin(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");
		if (StringUtils.isNotEmpty(echostr)) {
			boolean check = checkAuthentication(signature, timestamp, nonce);
			if (check) {
				logger.info("消息来源为微信服务器");
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write(echostr);
				response.getWriter().flush();
			}
		} else {
			// boolean check=checkAuthentication(signature,timestamp,nonce);
			// if(!check){
			// return null;
			// }
			// String content=convertStreamToString(request.getInputStream());
			// System.out.println("微信发送的消息为："+content);
			// if(content!=null){
			SAXReader reader = new SAXReader();
			try {
				Document document = reader.read(request.getInputStream());
				Element root = document.getRootElement();
				String fromUsername = root.elementText("FromUserName"); // 取得发送者
				String toUsername = root.elementText("ToUserName"); // 取得接收者
				String userMsgType = root.elementText("MsgType");
				String userMsgEvent = root.elementText("Event");
				String result = "";
				if ("event".equals(userMsgType)) {
					if ("subscribe".equals(userMsgEvent)) {
						logger.info(fromUsername + "订阅消息");
						result = helper(toUsername, fromUsername);
					} else {
						StringBuffer sb = new StringBuffer();
						sb.append("<xml>");
						sb.append("<ToUserName>" + fromUsername + "</ToUserName>");
						sb.append("<FromUserName>" + toUsername + "</FromUserName>");
						sb.append("<CreateTime>" + new Date().getTime() + "</CreateTime>");
						sb.append("<MsgType><![CDATA[text]]></MsgType>");
						sb.append("<MsgType><![CDATA[感谢你的关注！]]></MsgType>");
						sb.append("</xml>");
						result = sb.toString();
						logger.info(fromUsername + "取消订阅");
					}
				} else {
					String keyword = new String(root.elementTextTrim("Content").getBytes(), "UTF-8");
					if (keyword.contains("?") || keyword.contains("帮助")) {
						result = helper(toUsername, fromUsername);

					} else if (keyword.contains("官网")) {
						logger.debug("官网查询");
						result = getSite(toUsername, fromUsername);
					} else if (keyword.contains("天气")) {
						logger.debug("天气查询");
						int index = keyword.indexOf("天气");
						String city = keyword.substring(0, index);
						if (StringUtils.isNotEmpty(city))
							result = getWeather(city, toUsername, fromUsername);
					} else if (keyword.contains("体育")) {
						logger.debug("体育查询");
						result = getSports(toUsername, fromUsername);
					} else if (keyword.contains("彩票")) {
						logger.debug("彩票查询");
						int index = keyword.indexOf("票");
						String name = keyword.substring(index + 1);
						if (StringUtils.isNotEmpty(name))
							result = getTickets(name, toUsername, fromUsername);
					} else if (keyword.contains("手机")) {
						logger.debug("手机查询");
						int index = keyword.indexOf("机");
						String phone = keyword.substring(index + 1);
						if (StringUtils.isNotEmpty(phone))
							result = getPhone(phone, toUsername, fromUsername);
						// }else if(keyword.contains("公交")){
						// logger.debug("公交查询");
						// int index=keyword.indexOf("交");
						// String line=keyword.substring(index+1);
						// int index1=keyword.indexOf("公");
						// String city=keyword.substring(0,index1);
						// if(StringUtils.isNotEmpty(city)&&StringUtils.isNotEmpty(line))
						// result=getTransit(city,line,toUsername, fromUsername);
					} else if (keyword.contains("股票")) {
						logger.debug("股票查询");
						int index = keyword.indexOf("票");
						String code = keyword.substring(index + 1);
						if (StringUtils.isNotEmpty(code))
							result = getStock(code, toUsername, fromUsername);
					} else if (keyword.contains("座")) {
						logger.debug("星座查询");
						result = getStar(keyword, toUsername, fromUsername);
					} else {
						result = helper(toUsername, fromUsername);
					}
				}
				logger.debug("返回的消息为：" + result);
				response.setContentType("text/xml;charset=UTF-8");
				response.getWriter().write(result);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private String convertStreamToString(InputStream in, String chart)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in, chart));
			String line = "";
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	private boolean checkAuthentication(String signature, String timestamp, String nonce) {
		boolean result = false;
		String token = SystemConfig.getInstance().getProp("weixin.token");
		// 将获取到的参数放入数组
		String[] ArrTmp = { token, timestamp, nonce };
		// 按微信提供的方法，对数据内容进行排序
		Arrays.sort(ArrTmp);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ArrTmp.length; i++) {
			sb.append(ArrTmp[i]);
		}
		// 对排序后的字符串进行SHA-1加密
		String pwd = SHAUtils.SHA1(sb.toString());
		if (pwd.equals(signature)) {
			result = true;
		}
		return result;
	}

	private String helper(String toUser, String fromUser) {
		StringBuffer sb = new StringBuffer();
		String content = "【1.查看官网】:输入\"官网\"\n" + "【2.天气查询】:城市名+天气 例如输入\"北京天气\"\n"
				+ "【3.体育赛事查询】:例如输入\"体育赛事\"\n" + "【4.彩票查询】:彩票+彩票名 例如输入\"彩票双色球\"\n"
				+ "【5.手机归属地查询】:手机+手机号码 例如输入\"手机13812345678\"\n" +
				// "【6.公交查询】:城市名+公交+线路 例如输入\"深圳公交M240\"\n"+
				"【6.股票查询】:股票+股票代码 例如输入\"股票sh601006\"\n" + "【7.星座运势查询】:星座名称 例如输入\"摩羯座\"\n";
		sb.append("<xml>");
		sb.append("<ToUserName>" + fromUser + "</ToUserName>");
		sb.append("<FromUserName>" + toUser + "</FromUserName>");
		sb.append("<CreateTime>" + new Date().getTime() + "</CreateTime>");
		sb.append("<MsgType><![CDATA[text]]></MsgType>");
		sb.append("<Content><![CDATA[" + content + "]]></Content>");
		sb.append("</xml>");
		return sb.toString();
	}

	private String getSite(String toUser, String fromUser) {
		StringBuffer sb = new StringBuffer();
		String url = SystemConfig.getInstance().getProp("site.url");
		String content = "欢迎光临我的官网";
		sb.append("<xml>");
		sb.append("<ToUserName>" + fromUser + "</ToUserName>");
		sb.append("<FromUserName>" + toUser + "</FromUserName>");
		sb.append("<CreateTime>" + new Date().getTime() + "</CreateTime>");
		sb.append("<MsgType><![CDATA[news]]></MsgType>");
		sb.append("<ArticleCount>1</ArticleCount>");
		sb.append("<Articles><item>");
		sb.append("<Title><![CDATA[南的糊途]]></Title>");
		sb.append("<Description><![CDATA[" + content + "]]></Description>");
		sb.append("<PicUrl><![CDATA[" + url + "images/logo.jpg]]></PicUrl>");
		sb.append("<Url><![CDATA[" + url + "view/index]]></Url>");
		sb.append("</item></Articles>");
		sb.append("</xml>");
		return sb.toString();
	}

	private String getWeather(String city, String toUser, String fromUser) {
		StringBuffer sb = new StringBuffer();
		String code = WeatherConfig.getInstance().getCityCode(city);
		if (code == null || "".equals(code)) {
			return null;
		}
		String url = SystemConfig.getInstance().getProp("weather.url")
				.replaceAll("<cityCode>", code);
		String content = "";
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			InputStream in = response.getEntity().getContent();
			String result = convertStreamToString(in, "utf-8");
			Gson gson = new Gson();
			Weather weather = gson.fromJson(result, Weather.class);
			content = weather.getWeatherinfo().getCity() + "\n" + "温度："
					+ weather.getWeatherinfo().getTemp2() + "-"
					+ weather.getWeatherinfo().getTemp1() + "\n" + "天气："
					+ weather.getWeatherinfo().getWeather();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sb.append("<xml>");
		sb.append("<ToUserName>" + fromUser + "</ToUserName>");
		sb.append("<FromUserName>" + toUser + "</FromUserName>");
		sb.append("<CreateTime>" + new Date().getTime() + "</CreateTime>");
		sb.append("<MsgType><![CDATA[text]]></MsgType>");
		sb.append("<Content><![CDATA[" + content + "]]></Content>");
		sb.append("</xml>");
		return sb.toString();
	}

	private String getSports(String toUser, String fromUser) {
		String url = SystemConfig.getInstance().getProp("sports.url");
		StringBuffer result = new StringBuffer();
		try {
			org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
			Elements ul = doc.select("ul#list01");
			if (ul != null && ul.size() > 0) {
				Elements li = ul.get(0).getElementsByTag("li");
				for (org.jsoup.nodes.Element e : li) {
					String name = e.select("span.span01").get(0).text();
					String group = e.select("span.span02").get(0).text();
					Elements element = e.select("span.span03").get(0).getElementsByTag("a");
					String time = "";
					String score = "";
					if (element != null && element.size() > 0) {
						String str = e.select("span.span03").get(0).text();
						if (str.contains("日")) {
							score = str.substring(0, 3);
							time = e.select("span.span04").get(0).text();
						} else {
							score = element.get(0).text();
						}
					} else {
						score = e.select("span.span03").get(0).text();
						time = e.select("span.span04").get(0).text();
					}
					result.append(name + ":");
					result.append(group.replaceAll(" ", "VS"));
					if (!score.contains("日")) {
						if ("".equals(score)) {
							result.append(score + " " + time);
						} else
							result.append(score.replaceAll(" ", ":"));
					} else
						result.append(score + " " + time);
					result.append("\n");
				}
			}
			// Elements div=doc.select("div.matchnav_body");
			// Elements els=doc.select("div.matchnav_cell");
			// if(els!=null&&els.size()>0){
			// for(org.jsoup.nodes.Element e:els){
			// String name=e.select("div.matchnav_classify matchnav_football").get(0).text();
			// Elements p=e.select("div.matchnav_info1").get(0).getElementsByTag("p");
			// String time=p.get(0).text();
			// String date=p.get(1).text();
			// Elements p2=e.select("div.matchnav_info2").get(0).getElementsByTag("p");
			// String group1=p2.get(0).getElementsByTag("span").get(0).text();
			// String score1=p2.get(0).getElementsByTag("span").get(1).text();
			// String group2=p2.get(1).getElementsByTag("span").get(0).text();
			// String score2=p2.get(1).getElementsByTag("span").get(1).text();
			// result.append(name+":");
			// result.append(date+" "+time);
			// result.append(group1+"VS"+group2);
			// if(StringUtils.isNotEmpty(score2))
			// result.append(" "+score1+":"+score2);
			// }
			// }
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		sb.append("<ToUserName>" + fromUser + "</ToUserName>");
		sb.append("<FromUserName>" + toUser + "</FromUserName>");
		sb.append("<CreateTime>" + new Date().getTime() + "</CreateTime>");
		sb.append("<MsgType><![CDATA[text]]></MsgType>");
		sb.append("<Content><![CDATA[" + result.toString() + "]]></Content>");
		sb.append("</xml>");
		return sb.toString();
	}

	private String getTickets(String s, String toUser, String fromUser) {

		String url = SystemConfig.getInstance().getProp("tickets.url");
		StringBuffer result = new StringBuffer();
		try {
			org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
			Elements table = doc.select("table.kj_tab");
			if (table != null && table.size() > 0) {
				for (org.jsoup.nodes.Element e : table) {
					Elements trs = e.getElementsByTag("tr");
					for (org.jsoup.nodes.Element tr : trs) {
						if (tr.hasClass("title") || tr.hasClass("czbgcolor"))
							continue;
						Elements tds = tr.getElementsByTag("td");
						String name = tds.get(0).getElementsByTag("a").text();
						if (!s.equals(name))
							continue;
						result.append(name + ":");
						String periods = tds.get(1).getElementsByTag("a").text();
						result.append("第" + periods);
						result.append("\n");
						String time = tds.get(2).text();
						result.append(time + "\n");
						Elements spans = tds.get(3).select("div.ballbg").get(0)
								.getElementsByTag("span");
						String ball = "";
						for (org.jsoup.nodes.Element span : spans) {
							ball += span.text() + " ";
						}
						result.append("开奖号码:" + ball);
					}
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		sb.append("<ToUserName>" + fromUser + "</ToUserName>");
		sb.append("<FromUserName>" + toUser + "</FromUserName>");
		sb.append("<CreateTime>" + new Date().getTime() + "</CreateTime>");
		sb.append("<MsgType><![CDATA[text]]></MsgType>");
		sb.append("<Content><![CDATA[" + result.toString() + "]]></Content>");
		sb.append("</xml>");
		return sb.toString();
	}

	private String getPhone(String phone, String toUser, String fromUser) {
		StringBuffer sb = new StringBuffer();
		String url = SystemConfig.getInstance().getProp("phone.url").replaceAll("<phone>", phone);
		String content = "";
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			InputStream in = response.getEntity().getContent();
			SAXReader reader = new SAXReader();
			Document doc = reader.read(in);
			Element root = doc.getRootElement();
			Element result = root.element("result");
			if ("1".equals(root.elementText("success"))) {
				String type = result.elementText("ctype");
				String address = result.elementText("att");
				content = phone + ":" + address + " " + type;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		sb.append("<xml>");
		sb.append("<ToUserName>" + fromUser + "</ToUserName>");
		sb.append("<FromUserName>" + toUser + "</FromUserName>");
		sb.append("<CreateTime>" + new Date().getTime() + "</CreateTime>");
		sb.append("<MsgType><![CDATA[text]]></MsgType>");
		sb.append("<Content><![CDATA[" + content + "]]></Content>");
		sb.append("</xml>");
		return sb.toString();
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private String getTransit(String city, String line, String toUser, String fromUser) {
		StringBuffer sb = new StringBuffer();
		String url = SystemConfig.getInstance().getProp("transit.url").replaceAll("<city>", city)
				.replaceAll("<line>", line);
		StringBuffer content = new StringBuffer();
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			InputStream in = response.getEntity().getContent();
			SAXReader reader = new SAXReader();
			Document doc = reader.read(in);
			Element root = doc.getRootElement();
			Element lines = root.element("lines");
			List<Element> list = lines.elements("line");
			if (list != null && list.size() > 0) {
				Element e = list.get(0);
				// String name=e.elementText("name");
				// String info=e.elementText("info");
				String stats = e.elementText("stats");
				// content.append(name+"\n");
				// content.append(info+"\n");
				content.append(stats);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		sb.append("<xml>");
		sb.append("<ToUserName>" + fromUser + "</ToUserName>");
		sb.append("<FromUserName>" + toUser + "</FromUserName>");
		sb.append("<CreateTime>" + new Date().getTime() + "</CreateTime>");
		sb.append("<MsgType><![CDATA[text]]></MsgType>");
		sb.append("<Content><![CDATA[" + content.toString() + "]]></Content>");
		sb.append("</xml>");
		return sb.toString();
	}

	private String getStock(String code, String toUser, String fromUser) {
		StringBuffer sb = new StringBuffer();
		String url = SystemConfig.getInstance().getProp("stock.url").replaceAll("<code>", code);
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		StringBuffer sb1 = new StringBuffer();
		try {
			HttpResponse response = client.execute(get);
			String str = convertStreamToString(response.getEntity().getContent(), "gbk");
			int index = str.indexOf("\"");
			String[] s = str.substring(index).split(",");
			sb1.append(s[0]).append(" 今日开盘价：" + s[1] + " 昨日收盘价：" + s[2] + "\n");
			sb1.append("当前价：" + s[3] + " 今日最高价：" + s[4] + " 今日最低价：" + s[5] + "\n");
			sb1.append("时间：" + s[s.length - 3] + " " + s[s.length - 2]);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sb.append("<xml>");
		sb.append("<ToUserName>" + fromUser + "</ToUserName>");
		sb.append("<FromUserName>" + toUser + "</FromUserName>");
		sb.append("<CreateTime>" + new Date().getTime() + "</CreateTime>");
		sb.append("<MsgType><![CDATA[text]]></MsgType>");
		sb.append("<Content><![CDATA[" + sb1.toString() + "]]></Content>");
		sb.append("</xml>");
		return sb.toString();
	}

	private String getStar(String star, String toUser, String fromUser) {
		int index = 0;
		for (int i = 0; i < stars.length; i++) {
			if (stars[i].equals(star)) {
				index = (i + 1);
				break;
			}
		}
		if (index == 0)
			return "";
		String url = SystemConfig.getInstance().getProp("star.url");
		StringBuffer result = new StringBuffer();
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		try {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("xingzuo", index + ""));
			nvps.add(new BasicNameValuePair("type", "d"));
			post.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = client.execute(post);
			String str = convertStreamToString(response.getEntity().getContent(), "gbk");
			System.out.println(str);
			org.jsoup.nodes.Document doc = Jsoup.parse(str);
			Elements tds = doc.select("td.wz12_3B07");
			if (tds != null && tds.size() > 0) {
				String text = tds.get(1).text();
				String[] s = text.split(" ");
				result.append(s[1] + "\n");
				result.append(s[2] + s[3] + "\n");
				result.append(s[4] + s[5] + "\n");
				result.append(s[6] + s[7] + "\n");
				result.append(s[8] + s[9] + "\n");
				result.append(s[10] + s[11] + "\n");
				result.append(s[12] + s[13] + "\n");
				result.append(s[14] + s[15] + "\n");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		sb.append("<ToUserName>" + fromUser + "</ToUserName>");
		sb.append("<FromUserName>" + toUser + "</FromUserName>");
		sb.append("<CreateTime>" + new Date().getTime() + "</CreateTime>");
		sb.append("<MsgType><![CDATA[text]]></MsgType>");
		sb.append("<Content><![CDATA[" + result.toString() + "]]></Content>");
		sb.append("</xml>");
		return sb.toString();
	}
}