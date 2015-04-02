/**
 * InformationServiceImpl.java	  V1.0   2014-11-29 下午3:09:53
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

import com.yln.game.model.Information;
import com.yln.game.service.InformationService;
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
@Service("informationService")
public class InformationServiceImpl implements InformationService {

	@Autowired
	private HibernateTemplate template;

	@SuppressWarnings("unchecked")
	@Override
	public List<Information> getAll() {
		return template.find("from Information");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<Information> getByPage(Page<Information> page, String startTime, String endTime) {
		final int currentPage = page.getCurrentPage();
		final int size = page.getShowCount();
		final String sort = page.getSortName();
		final String order = page.getSortOrder();
		StringBuffer sql = new StringBuffer();
		sql.append("from Information");
		if (!StringUtils.isEmpty(startTime)) {
			sql.append(" where date_format(time,'%Y-%m-%d')>= '" + startTime + "'");
		}
		if (!StringUtils.isEmpty(endTime)) {
			sql.append(" date_format(time,'%Y-%m-%d')<= '" + endTime + "'");
		}
		if (!StringUtils.isEmpty(sort)) {
			sql.append(" order by " + sort);
		}
		if (!StringUtils.isEmpty(order)) {
			sql.append(" " + order);
		}
		final String hql = sql.toString();
		page.setTotalResult(template.find(hql).size());
		List<Information> list = template.executeFind(new HibernateCallback() {

			@Override
			public List<Information> doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query q = session.createQuery(hql);
				q.setFirstResult((currentPage - 1) * size);
				q.setMaxResults(size);
				List<Information> list = q.list();
				return list;
			}
		});
		page.setResultList(list);
		return page;
	}

	@Override
	public long save(Information info) {
		return (Long) template.save(info);
	}

	@Override
	public void update(Information info) {
		template.update(info);
	}

	@Override
	public void delete(Information info) {
		template.delete(info);
	}

	@Override
	public Information getById(long id) {
		return template.get(Information.class, id);
	}

}
