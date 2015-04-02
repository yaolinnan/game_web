/**
 * Page.java	  V1.0   2014-5-13 下午8:45:34
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.util;

import java.io.Serializable;
import java.util.List;

/**
 * descrition：
 * 
 * @author yaolinnan
 * 
 *         <p>
 *         modify history:
 *         </p>
 */
public class Page<T> implements Serializable {

	private static final long serialVersionUID = 4346563701883548897L;

	public Page() {
		showCount = 10;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public int getTotalPage() {
		if (totalResult % showCount == 0)
			totalPage = totalResult / showCount;
		else
			totalPage = totalResult / showCount + 1;
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalResult() {
		return totalResult;
	}

	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}

	public int getCurrentPage() {
		if (currentPage <= 0)
			currentPage = 1;
		// if(currentPage > getTotalPage())
		// currentPage = getTotalPage();
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getPageStr() {
		StringBuffer sb = new StringBuffer();
		if (totalResult > 0) {
			sb.append("\t<ul>\n");
			if (currentPage == 1) {
				sb.append("\t<li class=\"pageinfo\">\u9996\u9875</li>\n");
				sb.append("\t<li class=\"pageinfo\">\u4E0A\u9875</li>\n");
			} else {
				sb.append("\t<li><a href=\"javascript:scroll(0,0)\" mce_href=\"javascript:scroll(0,0)\" onclick=\"nPage(1)\">\u9996\u9875</a></li>\n");
				sb.append((new StringBuilder())
						.append("\t<li><a href=\"javascript:scroll(0,0)\" mce_href=\"javascript:scroll(0,0)\" onclick=\"nPage(")
						.append(currentPage - 1).append(")\">\u4E0A\u9875</a></li>\n").toString());
			}
			int showTag = 3;
			int startTag = 1;
			int endTag = totalPage;
			if (totalPage > showTag)
				if (currentPage == 1) {
					startTag = 1;
					endTag = showTag;
				} else if (currentPage == totalPage) {
					startTag = (currentPage - showTag) + 1;
					endTag = currentPage;
				} else {
					startTag = currentPage - 1;
					endTag = (currentPage + showTag) - 2;
				}
			for (int i = startTag; i <= endTag; i++)
				if (currentPage == i)
					sb.append((new StringBuilder()).append("<li class=\"current\">").append(i)
							.append("</li>\n").toString());
				else
					sb.append((new StringBuilder())
							.append("\t<li><a href=\"javascript:scroll(0,0)\" mce_href=\"javascript:scroll(0,0)\" onclick=\"nPage(")
							.append(i).append(")\">").append(i).append("</a></li>\n").toString());

			if (currentPage == totalPage) {
				sb.append("\t<li class=\"pageinfo\">\u4E0B\u9875</li>\n");
				sb.append("\t<li class=\"pageinfo\">\u5C3E\u9875</li>\n");
			} else {
				sb.append((new StringBuilder())
						.append("\t<li><a href=\"javascript:scroll(0,0)\" mce_href=\"javascript:scroll(0,0)\" onclick=\"nPage(")
						.append(currentPage + 1).append(")\">\u4E0B\u9875</a></li>\n").toString());
				sb.append((new StringBuilder())
						.append("\t<li><a href=\"javascript:scroll(0,0)\" mce_href=\"javascript:scroll(0,0)\" onclick=\"nPage(")
						.append(totalPage).append(")\">\u5C3E\u9875</a></li>\n").toString());
			}
			sb.append((new StringBuilder()).append("\t<li class=\"pageinfo\">\u7B2C")
					.append(currentPage).append("\u9875</li>\n").toString());
			sb.append((new StringBuilder()).append("\t<li class=\"pageinfo\">\u5171")
					.append(totalPage).append("\u9875</li>\n").toString());
			sb.append("</ul>\n");
		}
		pageStr = sb.toString();
		return pageStr;
	}

	public void setPageStr(String pageStr) {
		this.pageStr = pageStr;
	}

	public int getShowCount() {
		return showCount;
	}

	public void setShowCount(int showCount) {
		if (showCount > 0)
			this.showCount = showCount;
		else
			System.out.println("error: can't set Page.showCount to 0 ");
	}

	public int getCurrentResult() {
		currentResult = (getCurrentPage() - 1) * getShowCount();
		if (currentResult < 0)
			currentResult = 0;
		return currentResult;
	}

	public void setCurrentResult(int currentResult) {
		this.currentResult = currentResult;
	}

	public boolean isEntityOrField() {
		return entityOrField;
	}

	public void setEntityOrField(boolean entityOrField) {
		this.entityOrField = entityOrField;
	}

	private int showCount;
	private int totalPage;
	private int totalResult;
	private int currentPage;
	private int currentResult;
	private boolean entityOrField;
	private String pageStr;
	private String sortName;
	private String sortOrder;
	private List<T> resultList;

	public List<T> getResultList() {
		return resultList;
	}

	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}
}
