/**
 * NewsService.java	  V1.0   2014-11-29 下午2:44:49
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.service;

import java.util.List;

import com.yln.game.model.News;
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
public interface NewsService {

	public List<News> getAll();

	public Page<News> getByPage(Page<News> page, String startTime, String endTime);

	public long save(News news);

	public void update(News news);

	public void delete(News news);

	public News getById(long id);
}
