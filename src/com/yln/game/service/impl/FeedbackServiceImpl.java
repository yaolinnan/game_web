/**
 * MessageServiceImpl.java	  V1.0   2014-5-20 下午9:18:24
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yln.game.model.Feedback;
import com.yln.game.service.FeedbackService;
import com.yln.game.util.Page;

/**
 * descrition：
 * 
 * @author yaolinnan
 * 
 *         <p>
 *         modify history:
 *         </p>
 */
@Service("feedbackService")
public class FeedbackServiceImpl implements FeedbackService {

	@Autowired
	private HibernateTemplate template;

	@SuppressWarnings("unchecked")
	@Override
	public List<Feedback> getAll() {
		return template.find("from Feedback");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<Feedback> getByPage(Page<Feedback> page, String time) {
		final int currentPage = page.getCurrentPage();
		final int size = page.getShowCount();
		final String sort = page.getSortName();
		final String order = page.getSortOrder();
		StringBuffer sql = new StringBuffer();
		sql.append("from Feedback");
		if (!StringUtils.isEmpty(time)) {
			sql.append(" where date_format(createTime,'%Y-%m-%d')= '" + time + "'");
		}
		if (!StringUtils.isEmpty(sort)) {
			sql.append(" order by " + sort);
		}
		if (!StringUtils.isEmpty(order)) {
			sql.append(" " + order);
		}
		final String hql = sql.toString();
		page.setTotalResult(template.find(hql).size());
		List<Feedback> list = template.executeFind(new HibernateCallback() {

			@Override
			public List<Feedback> doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query q = session.createQuery(hql);
				q.setFirstResult((currentPage - 1) * size);
				q.setMaxResults(size);
				List<Feedback> list = q.list();
				return list;
			}
		});
		page.setResultList(list);
		return page;
	}

	@Override
	public void update(Feedback message) {
		template.update(message);
	}

	@Override
	public long save(Feedback message) {
		return (Long) template.save(message);
	}

	@Override
	public Feedback getById(long id) {
		return template.get(Feedback.class, id);
	}

}
