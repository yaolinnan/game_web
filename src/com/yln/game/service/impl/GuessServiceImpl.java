/**
 * GuessServiceImpl.java	  V1.0   2014-12-15 下午3:37:48
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

import com.yln.game.model.Guess;
import com.yln.game.service.GuessService;
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
@Service("guessService")
public class GuessServiceImpl implements GuessService {

	@Autowired
	private HibernateTemplate template;

	@SuppressWarnings("unchecked")
	@Override
	public List<Guess> getAll() {
		return template.find("from Guess");
	}

	@Override
	public Guess getById(long id) {
		return template.get(Guess.class, id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<Guess> getByPage(Page<Guess> page, String time) {
		final int currentPage = page.getCurrentPage();
		final int size = page.getShowCount();
		final String sort = page.getSortName();
		final String order = page.getSortOrder();
		StringBuffer sql = new StringBuffer();
		sql.append("from Guess");
		if (!StringUtils.isEmpty(time)) {
			sql.append(" where date_format(validTime,'%Y-%m-%d')>= '" + time + "'");
		}
		if (!StringUtils.isEmpty(sort)) {
			sql.append(" order by " + sort);
		}
		if (!StringUtils.isEmpty(order)) {
			sql.append(" " + order);
		}
		final String hql = sql.toString();
		page.setTotalResult(template.find(hql).size());
		List<Guess> list = template.executeFind(new HibernateCallback() {

			@Override
			public List<Guess> doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query q = session.createQuery(hql);
				q.setFirstResult((currentPage - 1) * size);
				q.setMaxResults(size);
				List<Guess> list = q.list();
				return list;
			}
		});
		page.setResultList(list);
		return page;
	}

	@Override
	public long save(Guess guess) {
		return (Long) template.save(guess);
	}

	@Override
	public void update(Guess guess) {
		template.update(guess);
	}

	@Override
	public void delete(Guess guess) {
		template.delete(guess);
	}

}
