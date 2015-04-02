/**
 * StategyServiceImpl.java	  V1.0   2014-11-29 下午3:38:44
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

import com.yln.game.model.Stategy;
import com.yln.game.service.StategyService;
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
@Service("stategyService")
public class StategyServiceImpl implements StategyService {

	@Autowired
	private HibernateTemplate template;

	@SuppressWarnings("unchecked")
	@Override
	public List<Stategy> getAll() {
		return template.find("from Stategy");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<Stategy> getByPage(Page<Stategy> page, String game) {
		final int currentPage = page.getCurrentPage();
		final int size = page.getShowCount();
		final String sort = page.getSortName();
		final String order = page.getSortOrder();
		StringBuffer sql = new StringBuffer();
		sql.append("from Stategy");
		if (!StringUtils.isEmpty(game)) {
			sql.append(" where game like %'" + game + "'%");
		}
		if (!StringUtils.isEmpty(sort)) {
			sql.append(" order by " + sort);
		}
		if (!StringUtils.isEmpty(order)) {
			sql.append(" " + order);
		}
		final String hql = sql.toString();
		page.setTotalResult(template.find(hql).size());
		List<Stategy> list = template.executeFind(new HibernateCallback() {

			@Override
			public List<Stategy> doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query q = session.createQuery(hql);
				q.setFirstResult((currentPage - 1) * size);
				q.setMaxResults(size);
				List<Stategy> list = q.list();
				return list;
			}
		});
		page.setResultList(list);
		return page;
	}

	@Override
	public long save(Stategy stategy) {
		return (Long) template.save(stategy);
	}

	@Override
	public void update(Stategy stategy) {
		template.update(stategy);
	}

	@Override
	public void delete(Stategy stategy) {
		template.delete(stategy);
	}

	@Override
	public Stategy getById(long id) {
		return template.get(Stategy.class, id);
	}

}
