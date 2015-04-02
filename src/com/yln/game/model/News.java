/**
 * News.java	  V1.0   2014-11-24 下午4:39:19
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "game_news")
public class News {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", length = 20, nullable = false)
	private long id;

	@Column(name = "title", length = 100, nullable = false)
	private String title;

	@Column(name = "image", length = 100)
	private String image;

	@Column(name = "time")
	private Date time;

	@Column(name = "type", length = 30)
	private String type;

	@Column(name = "description", length = 200)
	private String description;

	@Column(name = "content", length = 2000, nullable = false)
	private String content;

	@ManyToOne(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@JoinColumn(name = "source_id")
	private Source source;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == News.class) {
			News news = (News) obj;
			return news.id == id;
		}
		return false;
	}

}
