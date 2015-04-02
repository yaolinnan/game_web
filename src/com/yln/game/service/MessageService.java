/**
 * MessageService.java	  V1.0   2014-12-11 下午2:32:11
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.service;

import java.util.List;

import com.yln.game.model.Message;
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
public interface MessageService {

	public List<Message> getAll();

	public Page<Message> getByPage(Page<Message> page, String status);

	public List<Message> getByUser(long userId, String status);

	public long save(Message msg);

	public void delete(Message msg);

	public Message getById(long id);
}
