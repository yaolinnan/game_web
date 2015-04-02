/**
 * UserService.java	  V1.0   2014-5-11 下午2:07:11
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.service;

import java.util.List;

import com.yln.game.model.User;
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
public interface UserService {

	public User login(String name, String password, String role);

	public long save(User user);

	public void update(User user);

	public void delete(long id);

	public User getById(long id);

	public List<User> getAll();

	public List<User> getByRole(String role);

	public Page<User> getByPage(Page<User> page, String name);

	public User getByName(String name);

}
