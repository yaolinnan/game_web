/**
 * Guess.java	  V1.0   2014-12-15 下午3:15:54
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * descrition：
 * 
 * @author yaolinnan
 * 
 *         <p>
 *         modify history:
 *         </p>
 */
@Entity
@Table(name = "guess")
public class Guess {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", length = 20, nullable = false)
	private long id;

	@Column(name = "title", length = 100)
	private String title;

	@Column(name = "content", length = 200, nullable = false)
	private String content;

	@Column(name = "options", length = 100, nullable = false)
	private String options;

	@Column(name = "cost", length = 11, nullable = false)
	private int cost;

	@Column(name = "award", length = 11, nullable = false)
	private int award;

	@Column(name = "createTime")
	private Date createTime;

	@Column(name = "validTime")
	private Date validTime;

	@Column(name = "answer", length = 20, nullable = false)
	private String answer;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getAward() {
		return award;
	}

	public void setAward(int award) {
		this.award = award;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getValidTime() {
		return validTime;
	}

	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
