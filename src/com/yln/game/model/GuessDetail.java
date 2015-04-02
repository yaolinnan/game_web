/**
 * GuessDetail.java	  V1.0   2014-12-15 下午3:27:49
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.model;

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
@Table(name = "guess_detail")
public class GuessDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", length = 20, nullable = false)
	private long id;

	@Column(name = "option", length = 20, nullable = false)
	private String option;

	@ManyToOne(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@JoinColumn(name = "guess_id")
	private Guess guess;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Guess getGuess() {
		return guess;
	}

	public void setGuess(Guess guess) {
		this.guess = guess;
	}

}
