/**
 * InformationService.java	  V1.0   2014-11-29 下午3:08:33
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.service;

import java.util.List;

import com.yln.game.model.Information;
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
public interface InformationService {

	public List<Information> getAll();

	public Page<Information> getByPage(Page<Information> page, String startTime, String endTime);

	public long save(Information info);

	public void update(Information info);

	public void delete(Information info);

	public Information getById(long id);
}
