/**
 * StategyService.java	  V1.0   2014-11-29 下午3:35:24
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.service;

import java.util.List;

import com.yln.game.model.Stategy;
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
public interface StategyService {

	public List<Stategy> getAll();

	public Page<Stategy> getByPage(Page<Stategy> page, String game);

	public long save(Stategy stategy);

	public void update(Stategy stategy);

	public void delete(Stategy stategy);

	public Stategy getById(long id);
}
