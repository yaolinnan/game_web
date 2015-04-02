/**
 * NewsCommentServiceImpl.java	  V1.0   2014-12-29 上午10:53:04
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yln.game.model.NewsComment;
import com.yln.game.service.NewsCommentService;

/**
 * descrition：
 * 
 * @author yaolinnan
 * 
 *         <p>
 *         modify history:
 *         </p>
 */
@Service("newsCommentService")
public class NewsCommentServiceImpl implements NewsCommentService {

	@Autowired
	private HibernateTemplate template;

	@SuppressWarnings("unchecked")
	@Override
	public List<NewsComment> getAll() {
		return template.find("from NewsComment");
	}

	@Override
	public NewsComment getById(long id) {
		return template.get(NewsComment.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NewsComment> getByUser(long userId) {
		return template.find("from NewsComment where user.id=" + userId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NewsComment> getByComment(long newsId) {
		return template.find("from NewsComment where news.id=" + newsId);
	}

	@Override
	public long add(NewsComment nc) {
		return (Long) template.save(nc);
	}

	@Override
	public void delete(NewsComment nc) {
		template.delete(nc);
	}

}
