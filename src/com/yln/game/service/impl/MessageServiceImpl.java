/**
 * MessageServiceImpl.java	  V1.0   2014-12-11 下午2:36:50
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

import com.yln.game.model.Message;
import com.yln.game.service.MessageService;
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
@Service("messageService")
public class MessageServiceImpl implements MessageService {

	@Autowired
	private HibernateTemplate template;

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> getAll() {
		return template.find("from Message");
	}

	@Override
	public Page<Message> getByPage(Page<Message> page, String status) {
		final int currentPage = page.getCurrentPage();
		final int size = page.getShowCount();
		final String sort = page.getSortName();
		final String order = page.getSortOrder();
		StringBuffer sql = new StringBuffer();
		sql.append("from Message");
		if (!StringUtils.isEmpty(status)) {
			sql.append(" where status= '" + status + "'");
		}
		if (!StringUtils.isEmpty(sort)) {
			sql.append(" order by " + sort);
		}
		if (!StringUtils.isEmpty(order)) {
			sql.append(" " + order);
		}
		final String hql = sql.toString();
		page.setTotalResult(template.find(hql).size());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<Message> list = template.executeFind(new HibernateCallback() {

			@Override
			public List<Message> doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query q = session.createQuery(hql);
				q.setFirstResult((currentPage - 1) * size);
				q.setMaxResults(size);
				List<Message> list = q.list();
				return list;
			}
		});
		page.setResultList(list);
		return page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> getByUser(long userId, String status) {
		return template.find("from Message where status='" + status + "' and user.id=" + userId);
	}

	@Override
	public long save(Message msg) {
		return (Long) template.save(msg);
	}

	@Override
	public void delete(Message msg) {
		template.delete(msg);
	}

	@Override
	public Message getById(long id) {
		return template.get(Message.class, id);
	}

}
