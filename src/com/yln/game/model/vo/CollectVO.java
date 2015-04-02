/**
 * CollectVO.java	  V1.0   2014-12-25 下午3:39:30
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.model.vo;

import java.util.List;

import com.yln.game.model.Information;
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
public class CollectVO {

	private List<Information> infoes;

	private List<News> news;

	private List<Stategy> stategys;

	public List<Information> getInfoes() {
		return infoes;
	}

	public void setInfoes(List<Information> infoes) {
		this.infoes = infoes;
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

}
