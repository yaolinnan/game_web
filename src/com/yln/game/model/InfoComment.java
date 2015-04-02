/**
 * NewsComment.java	  V1.0   2014-12-29 上午10:18:33
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
@Table(name = "comment_info")
public class InfoComment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", length = 20, nullable = false)
	private long id;

	@Column(name = "comment", length = 1000, nullable = false)
	private String comment;

	@Column(name = "status", length = 2, nullable = false)
	private String status;

	@Column(name = "createTime")
	private Date createTime;

	@ManyToOne(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@JoinColumn(name = "info_id")
	private Information info;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Information getInfo() {
		return info;
	}

	public void setInfo(Information info) {
		this.info = info;
	}

}
