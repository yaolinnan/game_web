/**
 * NewsServiceImpl.java	  V1.0   2014-11-29 下午2:47:24
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

import com.yln.game.model.News;
import com.yln.game.service.NewsService;
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
@Service("newsService")
public class NewsServiceImpl implements NewsService {

	@Autowired
	private HibernateTemplate template;

	@SuppressWarnings("unchecked")
	@Override
	public List<News> getAll() {
		return template.find("from News");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<News> getByPage(Page<News> page, String startTime, String endTime) {
		final int currentPage = page.getCurrentPage();
		final int size = page.getShowCount();
		final String sort = page.getSortName();
		final String order = page.getSortOrder();
		StringBuffer sql = new StringBuffer();
		sql.append("from News");
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
		List<News> list = template.executeFind(new HibernateCallback() {

			@Override
			public List<News> doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query q = session.createQuery(hql);
				q.setFirstResult((currentPage - 1) * size);
				q.setMaxResults(size);
				List<News> list = q.list();
				return list;
			}
		});
		page.setResultList(list);
		return page;
	}

	@Override
	public long save(News news) {
		return (Long) template.save(news);
	}

	@Override
	public void update(News news) {
		template.update(news);
	}

	@Override
	public void delete(News news) {
		template.delete(news);
	}

	@Override
	public News getById(long id) {
		return template.get(News.class, id);
	}

}
