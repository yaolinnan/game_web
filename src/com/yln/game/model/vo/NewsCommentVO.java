/**
 * NewsCommentVO.java	  V1.0   2014-12-29 上午11:13:13
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.model.vo;

import java.util.List;

import com.yln.game.model.NewsComment;

/**
 * descrition：
 * 
 * @author yaolinnan
 * 
 *         <p>
 *         modify history:
 *         </p>
 */
public class NewsCommentVO {

	private List<NewsComment> list;

	private String msg;

	private int size;

	public List<NewsComment> getList() {
		return list;
	}

	public void setList(List<NewsComment> list) {
		this.list = list;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
