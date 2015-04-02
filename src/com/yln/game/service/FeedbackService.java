/**
 * MessageService.java	  V1.0   2014-5-11 下午2:17:26
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.service;

import java.util.List;

import com.yln.game.model.Feedback;
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
public interface FeedbackService {

	public List<Feedback> getAll();

	public Page<Feedback> getByPage(Page<Feedback> page, String time);

	public void update(Feedback message);

	public long save(Feedback message);

	public Feedback getById(long id);
}
