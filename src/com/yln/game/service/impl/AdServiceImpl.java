/**
 * AdServiceImpl.java	  V1.0   2014-7-15 下午9:11:57
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yln.game.model.Ad;
import com.yln.game.service.AdService;

/**
 * descrition：
 * 
 * @author yaolinnan
 * 
 *         <p>
 *         modify history:
 *         </p>
 */
@Service("adService")
public class AdServiceImpl implements AdService {

	@Autowired
	private HibernateTemplate template;

	@Override
	public long save(Ad ad) {
		return (Long) template.save(ad);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ad> queryAll(int type) {
		String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		if (type == 0) {
			return template.find("from Ad where date_format(validTime,'%Y-%m-%d')>='" + now + "'");
		}
		return template.find("from Ad where date_format(validTime,'%Y-%m-%d')>='" + now
				+ "'and type=" + type);
	}

	@Override
	public void delete(long id) {
		template.delete(id);
	}

	@Override
	public Ad getById(long id) {
		return template.get(Ad.class, id);
	}

}
