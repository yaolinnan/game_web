/**
 * NewsVO.java	  V1.0   2014-12-1 下午3:27:33
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.model.vo;

import java.util.List;

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
public class StategyVO extends ReturnVO {

	private int currentPage;

	private int totalPage;

	private List<Stategy> list;

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List<Stategy> getList() {
		return list;
	}

	public void setList(List<Stategy> list) {
		this.list = list;
	}

}
