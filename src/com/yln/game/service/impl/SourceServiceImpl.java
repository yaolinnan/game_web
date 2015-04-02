/**
 * SourceServiceImpl.java	  V1.0   2014-11-29 下午3:05:20
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

import com.yln.game.model.Source;
import com.yln.game.service.SourceService;

/**
 * descrition：
 * 
 * @author yaolinnan
 * 
 *         <p>
 *         modify history:
 *         </p>
 */
@Service("sourceService")
public class SourceServiceImpl implements SourceService {

	@Autowired
	private HibernateTemplate template;

	@SuppressWarnings("unchecked")
	@Override
	public List<Source> getAll() {
		return template.find("from Source");
	}

	@Override
	public long save(Source source) {
		return (Long) template.save(source);
	}

	@Override
	public Source getById(long id) {
		return template.get(Source.class, id);
	}

	@Override
	public void update(Source source) {
		template.update(source);
	}

	@Override
	public void delete(Source source) {
		template.delete(source);
	}

}
