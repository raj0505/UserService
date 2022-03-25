package com.amdocs.services;

import java.util.List;

import com.amdocs.model.Account;
import com.amdocs.model.ResponseTemplateHandler;
import com.amdocs.model.User;

public interface UserService {

	public User createUser(User user);

	public User updateUser(User user, long userId);

	public User getUser(long userId);

	public List<User> getAllUser();

	public void deleteUser(long userId);

	public void deleteAllUser();

	/*
	 * public User addaccounttouser(User user,Account account,long userId,long
	 * accountId);
	 */

	public ResponseTemplateHandler getUserWithAccount(long userId);

}
