/**
 * InfoCommentServiceImpl.java	  V1.0   2014-12-29 上午11:02:34
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

import com.yln.game.model.InfoComment;
import com.yln.game.service.InfoCommentService;

/**
 * descrition：
 * 
 * @author yaolinnan
 * 
 *         <p>
 *         modify history:
 *         </p>
 */
@Service("infoCommentService")
public class InfoCommentServiceImpl implements InfoCommentService {

	@Autowired
	private HibernateTemplate template;

	@SuppressWarnings("unchecked")
	@Override
	public List<InfoComment> getAll() {
		return template.find("from InfoComment");
	}

	@Override
	public InfoComment getById(long id) {
		return template.get(InfoComment.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InfoComment> getByUser(long userId) {
		return template.find("from InfoComment where user.id=" + userId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InfoComment> getByComment(long infoId) {
		return template.find("from InfoComment where info.id=" + infoId);
	}

	@Override
	public long add(InfoComment ic) {
		return (Long) template.save(ic);
	}

	@Override
	public void delete(InfoComment ic) {
		template.delete(ic);
	}

}
