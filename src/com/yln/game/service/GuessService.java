/**
 * GuessService.java	  V1.0   2014-12-15 下午3:35:06
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.service;

import java.util.List;

import com.yln.game.model.Guess;
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
public interface GuessService {

	public List<Guess> getAll();

	public Guess getById(long id);

	public Page<Guess> getByPage(Page<Guess> page, String time);

	public long save(Guess guess);

	public void update(Guess guess);

	public void delete(Guess guess);
}
