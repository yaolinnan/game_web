/**
 * NewsCommentService.java	  V1.0   2014-12-29 上午10:45:35
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.service;

import java.util.List;

import com.yln.game.model.NewsComment;

/**
 * descrition：
 * 
 * @author yaolinnan
 * 
 *         <p>
 *         modify history:
 *         </p>
 */
public interface NewsCommentService {

	public List<NewsComment> getAll();

	public NewsComment getById(long id);

	public List<NewsComment> getByUser(long userId);

	public List<NewsComment> getByComment(long newsId);

	public long add(NewsComment nc);

	public void delete(NewsComment nc);
}
