/**
 * SourceService.java	  V1.0   2014-11-29 下午3:01:33
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.service;

import java.util.List;

import com.yln.game.model.Source;

/**
 * descrition：
 * 
 * @author yaolinnan
 * 
 *         <p>
 *         modify history:
 *         </p>
 */
public interface SourceService {

	public List<Source> getAll();

	public long save(Source source);

	public Source getById(long id);

	public void update(Source source);

	public void delete(Source source);
}
