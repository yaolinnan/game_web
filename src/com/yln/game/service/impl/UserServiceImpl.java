/**
 * UserServiceImpl.java	  V1.0   2014-5-11 下午2:20:16
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

import com.yln.game.model.User;
import com.yln.game.service.UserService;
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
@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private HibernateTemplate template;

	@SuppressWarnings("unchecked")
	@Override
	public User login(String name, String password, String role) {
		String sql = "from User where name='" + name + "' and password='" + password
				+ "' and role=" + role;
		List<User> list = template.find(sql);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@Override
	public long save(User user) {
		return (Long) template.save(user);
	}

	@Override
	public void update(User user) {
		template.update(user);
	}

	@Override
	public void delete(long id) {
		template.delete(id);
	}

	@Override
	public User getById(long id) {
		return template.get(User.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAll() {
		return template.find("from User");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Page<User> getByPage(Page<User> page, String name) {
		final int currentPage = page.getCurrentPage();
		final int size = page.getShowCount();
		final String sort = page.getSortName();
		final String order = page.getSortOrder();
		StringBuffer sql = new StringBuffer();
		sql.append("from User");
		if (!StringUtils.isEmpty(name)) {
			sql.append(" where name like '%" + name + "%'");
		}
		if (!StringUtils.isEmpty(sort)) {
			sql.append(" order by " + sort);
		}
		if (!StringUtils.isEmpty(order)) {
			sql.append(" " + order);
		}
		final String hql = sql.toString();
		page.setTotalResult(template.find(hql).size());
		List<User> list = template.executeFind(new HibernateCallback() {

			@Override
			public List<User> doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query q = session.createQuery(hql);
				q.setFirstResult((currentPage - 1) * size);
				q.setMaxResults(size);
				List<User> list = q.list();
				return list;
			}
		});
		page.setResultList(list);
		return page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public User getByName(String name) {
		List<User> list = template.find("from User where name='" + name + "'");
		if (list == null || list.size() == 0)
			return null;
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getByRole(String role) {
		return template.find("from User where role='" + role + "'");
	}

}
