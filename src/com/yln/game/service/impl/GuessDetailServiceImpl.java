/**
 * GuessDetailServiceImpl.java	  V1.0   2014-12-15 下午3:51:19
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

import com.yln.game.model.GuessDetail;
import com.yln.game.service.GuessDetailService;

/**
 * descrition：
 * 
 * @author yaolinnan
 * 
 *         <p>
 *         modify history:
 *         </p>
 */
@Service("guessDetailService")
public class GuessDetailServiceImpl implements GuessDetailService {

	@Autowired
	private HibernateTemplate template;

	@SuppressWarnings("unchecked")
	@Override
	public List<GuessDetail> getAll() {
		return template.find("from GuessDetail");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GuessDetail> getByGuess(long guessId, String option) {
		return template.find("from GuessDetail where guess.id=" + guessId + " and option='"
				+ option + "'");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GuessDetail> getByUser(long userId) {
		return template.find("from GuessDetail where user.id=" + userId);
	}

}
