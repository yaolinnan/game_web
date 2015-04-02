/**
 * AdService.java	  V1.0   2014-7-15 下午9:10:13
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.service;

import java.util.List;

import com.yln.game.model.Ad;

/**
 * descrition：
 * 
 * @author yaolinnan
 * 
 *         <p>
 *         modify history:
 *         </p>
 */
public interface AdService {

	public long save(Ad ad);

	public List<Ad> queryAll(int type);

	public void delete(long id);

	public Ad getById(long id);
}
