/*
 * AccountClient In UserService is used  to Connect with AccountService Url.
 */
package com.amdocs.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.amdocs.model.Account;
import com.amdocs.model.User;

import feign.Headers;

@FeignClient(name = "account-service")
public interface AccountClient {

	@GetMapping("/user")
	public User getUserWithAccount(long userId);

	@GetMapping("account/getallaccount")
	public List<Account> getUserAccount();

	@GetMapping("account/getaccount/{accountId}")
	public Account getUserSingleAccount(long accountId);
}
