/**
 * InfoCommentService.java	  V1.0   2014-12-29 上午10:49:37
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.service;

import java.util.List;

import com.yln.game.model.InfoComment;

/**
 * descrition：
 * 
 * @author yaolinnan
 * 
 *         <p>
 *         modify history:
 *         </p>
 */
public interface InfoCommentService {

	public List<InfoComment> getAll();

	public InfoComment getById(long id);

	public List<InfoComment> getByUser(long userId);

	public List<InfoComment> getByComment(long infoId);

	public long add(InfoComment ic);

	public void delete(InfoComment ic);
}
