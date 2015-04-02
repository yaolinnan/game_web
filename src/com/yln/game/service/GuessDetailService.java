/**
 * GuessDetailService.java	  V1.0   2014-12-15 下午3:45:30
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.service;

import java.util.List;

import com.yln.game.model.GuessDetail;

/**
 * descrition：
 * 
 * @author yaolinnan
 * 
 *         <p>
 *         modify history:
 *         </p>
 */
public interface GuessDetailService {

	public List<GuessDetail> getAll();

	public List<GuessDetail> getByGuess(long guessId, String option);

	public List<GuessDetail> getByUser(long userId);
}
