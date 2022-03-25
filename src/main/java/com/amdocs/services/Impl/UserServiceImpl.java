package com.amdocs.services.Impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.client.RestTemplate;

import com.amdocs.model.Account;
import com.amdocs.model.ResponseTemplateHandler;
import com.amdocs.model.User;

import com.amdocs.repository.UserRepository;
import com.amdocs.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	/*
	 * @Autowired private AccountRepository accountRepository;
	 */
	@Autowired
	private RestTemplate restTemplate;

	// Creating a user
	public User createUser(User user) {

		User local = (User) this.userRepository.findByUserId(user.getUserId());

		if (local != null) {
			System.out.println("user is already there");

		} else {
			local = this.userRepository.save(user);
		}

		return local;
	}

	@Override
	public User getUser(long userId) {
		// TODO Auto-generated method stub
		return this.userRepository.findById(userId).get();
	}

	@Override
	public void deleteUser(long userId) {

		this.userRepository.deleteById(userId);

	}

	@Override
	public void deleteAllUser() {

		this.userRepository.deleteAll();
	}

	@Override
	public List<User> getAllUser() {

		return this.userRepository.findAll();
	}

	@Override
	public User updateUser(User user, long userId) {

		User userToUpdate = (User) this.userRepository.findByUserId(user.getUserId());

		if (userToUpdate.getUserId() == user.getUserId()) {
			userToUpdate.setName(user.getName());
			userToUpdate.setEmailId(user.getEmailId());
			userToUpdate.setMobileNumber(user.getMobileNumber());
			userToUpdate.setSecondaryMobile(user.getSecondaryMobile());
			userToUpdate.setDateOfBirth(user.getDateOfBirth());
			userToUpdate.setGender(user.getGender());

			this.userRepository.save(user);

		}

		return userToUpdate;
	}

	/*
	 * @Override public User addaccounttouser(User user, Account account, long
	 * userId, long accountId) { // TODO Auto-generated method stub return null; }
	 */

	@Override
	public ResponseTemplateHandler getUserWithAccount(long userId) {
		ResponseTemplateHandler handler = new ResponseTemplateHandler();
		User user = this.userRepository.findById(userId).get();
		Account account = restTemplate.getForObject("http://account-service/account/getaccount/" + user.getAccountId(),
				Account.class);

		handler.setUser(user);
		handler.setAccount(account);
		return handler;
	}

	/*
	 * public User addaccounttouser(User user, Account account, long userId, long
	 * accountId) {
	 * 
	 * User userin=(User) this.userRepository.findById(userId).get(); Account
	 * accountto = this.accountRepository.findById(accountId).get();
	 * 
	 * 
	 * userin.getAccounts().add(accountto);
	 * 
	 * 
	 * accountto.setUser(userin);
	 * 
	 * 
	 * this.userRepository.save(userin);
	 * 
	 * 
	 * // this.accountRepository.save(account);
	 * 
	 * 
	 * return userin; }
	 * 
	 */

}
