package com.amdocs.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.amdocs.client.AccountClient;
import com.amdocs.client.AccountClient;
import com.amdocs.model.Account;
import com.amdocs.model.ResponseTemplateHandler;
import com.amdocs.model.User;

import com.amdocs.services.UserService;
import com.google.inject.PrivateBinder;
import com.netflix.discovery.converters.Auto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/user")
//@EnableFeignClients
public class UserController {
	@Autowired
	private UserService userService;

	/*
	 * publisher endpoint of kafka sing Kafka Template
	 */
	@Autowired
	private KafkaTemplate<String, User> kafkaTemplate;
	private static final String TOPIC = "kafka_user";

	@GetMapping("/publish/{userId}")
	public ResponseEntity<User> kafkaPublish(@PathVariable("userId") long userId) {
		User userPublished = this.userService.getUser(userId);

		kafkaTemplate.send(TOPIC, userPublished);
		return new ResponseEntity<User>(userPublished, HttpStatus.OK);
	}

	/*
	 * Publisher endpoint of kafka using spring cloud stream
	 */
	@Autowired
	private MessageChannel output;

	@PostMapping("/publish")
	public User publishEvent(@RequestBody User user) {
		output.send(org.springframework.messaging.support.MessageBuilder.withPayload(user).build());
		return user;
	}

	@GetMapping("/{userId}")
	public ResponseTemplateHandler getUserWithAccount(@PathVariable("userId") long userId) {
		return this.userService.getUserWithAccount(userId);
	}

	/*
	 * @Autowired private UserClient userClient;
	 * 
	 * @GetMapping("/{userId}") public User getUsers(@PathVariable("userId") long
	 * userId) { return this.userClient.getUserWithAccount(userId); }
	 * 
	 */
	@Autowired
	private AccountClient accountClient;

	@GetMapping("/account") // localhost:9001/user/account
	public ResponseEntity<List<Account>> getUserAccount() {
		List<Account> accounts = this.accountClient.getUserAccount();
		return new ResponseEntity<List<Account>>(accounts, HttpStatus.OK);
	}

	@GetMapping("/account/{accountId}") // localhost:9001/user/account/
	public ResponseEntity<Account> getUserSingleAccount(@PathVariable("accountId") long accountId) {
		Account account = this.accountClient.getUserSingleAccount(accountId);
		return new ResponseEntity<Account>(account, HttpStatus.OK);
	}

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

//@Autowired
//	private RestTemplate restTemplate;

	@ApiOperation(value = "Creates a new user", notes = "creates a new user with auto generated userid", response = User.class

	)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Service Not Available"),
			@ApiResponse(code = 500, message = "Internal Server Error"), @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 201, message = "Created") })
	@PostMapping("/createuser")
	public ResponseEntity<User> CreateUser(@Valid @RequestBody User user) {

		User createdUser = this.userService.createUser(user);

		logger.info("a new user is created with user id :" + createdUser.getUserId());

		return new ResponseEntity<User>(createdUser, HttpStatus.CREATED);
	}

	@ApiOperation(value = "gets a user from userid", notes = "please provide userid to get user details", response = User.class

	)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Service Not Available"),
			@ApiResponse(code = 500, message = "Internal Server Error"), @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 204, message = "No Content") })
	@GetMapping("/getuser/{userId}")
	public ResponseEntity<User> getUser(@Valid @PathVariable("userId") long userId) {

		User userRetrieved = this.userService.getUser(userId);
		// kafkaTemplate.send(TOPIC, userRetrieved);
		logger.info(" user is retrieved with user id :" + userRetrieved.getUserId() + " and username :"
				+ userRetrieved.getName());

		return new ResponseEntity<User>(userRetrieved, HttpStatus.OK);
	}

	@ApiOperation(value = "gets all user ", notes = "it fetches all user details", response = User.class

	)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Service Not Available"),
			@ApiResponse(code = 500, message = "Internal Server Error"), @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 204, message = "No Content") })
	@GetMapping("/getalluser")
	public ResponseEntity<List<User>> getAllUser() {
		logger.info("getting all user details :");
		List<User> allUser = this.userService.getAllUser();
		return new ResponseEntity<List<User>>(allUser, HttpStatus.OK);
	}

	@ApiOperation(value = "deletes a user   ", notes = "it deletes  user details from userid", response = User.class

	)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Service Not Available"),
			@ApiResponse(code = 500, message = "Internal Server Error"), @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 204, message = "No Content") })
	@DeleteMapping("/deleteuser/{userId}")
	public ResponseEntity deleteUser(@Valid @PathVariable("userId") long userId) {
		this.userService.deleteUser(userId);

		logger.info("user with userid" + userId + " is deleted from the database");

		return new ResponseEntity(HttpStatus.ACCEPTED);

	}

	@ApiOperation(value = "deletes all user   ", notes = "it deletes all  user details", response = User.class

	)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Service Not Available"),
			@ApiResponse(code = 500, message = "Internal Server Error"), @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 204, message = "No Content") })
	@DeleteMapping("/deletealluser")
	public ResponseEntity deleteAllUser() {
		this.userService.deleteAllUser();
		logger.info("All users are  deleted from the database");

		return new ResponseEntity(HttpStatus.ACCEPTED);
	}

	@ApiOperation(value = "updates a user   ", notes = "it updates   user details with userid", response = User.class

	)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Service Not Available"),
			@ApiResponse(code = 500, message = "Internal Server Error"), @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 204, message = "No Content") })
	@PutMapping("/updateuser/{userId}")
	public ResponseEntity<User> updateUser(@Valid @RequestBody User user, @PathVariable("userId") long userId) {

		User updatedUser = this.userService.updateUser(user, userId);

		logger.info("data for user with user id" + userId + "is updated Succesfully!!");
		return new ResponseEntity<User>(updatedUser, HttpStatus.ACCEPTED);

	}

	/*
	 * @PutMapping("/{userId}/{accountId}") public User
	 * addaccounttouser(@Valid @RequestBody User user,Account
	 * account, @PathVariable("userId") long userId,@PathVariable("accountId") long
	 * accountId) {
	 * 
	 * return this.userService.addaccounttouser(user, account, userId, accountId); }
	 */

}
