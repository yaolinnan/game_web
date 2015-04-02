/**
 * BasicData.java	  V1.0   2014-12-1 下午3:21:44
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.model.vo;

import java.util.List;

import com.yln.game.model.Ad;
import com.yln.game.model.Information;
import com.yln.game.model.Message;
import com.yln.game.model.News;
import com.yln.game.model.Stategy;

/**
 * descrition：
 * 
 * @author yaolinnan
 * 
 *         <p>
 *         modify history:
 *         </p>
 */
public class BasicData {

	private List<Ad> ads;

	private List<News> news;

	private List<Stategy> stategys;

	private List<Information> infos;

	private List<Message> messages;

	public List<Ad> getAds() {
		return ads;
	}

	public void setAds(List<Ad> ads) {
		this.ads = ads;
	}

	public List<News> getNews() {
		return news;
	}

	public void setNews(List<News> news) {
		this.news = news;
	}

	public List<Stategy> getStategys() {
		return stategys;
	}

	public void setStategys(List<Stategy> stategys) {
		this.stategys = stategys;
	}

	public List<Information> getInfos() {
		return infos;
	}

	public void setInfos(List<Information> infos) {
		this.infos = infos;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

}
