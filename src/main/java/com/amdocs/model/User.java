package com.amdocs.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(description = "Details about User")
@Data
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	@Column(length = 5)
	private long userId;

	@NotNull
	@Size(min = 2, max = 40, message = "Name cant be  left empty.It Should have at least2 characters")

	private String name;
	@NotNull
	@Size(max = 200, message = "Please write email in proper format")
	@Email
	private String emailId;
	@Column(length = 10)
	@NotNull
	@Size(min = 10, message = "Please write 10 digit mobile number")
	private String mobileNumber;
	@Size(min = 10, message = "Please write 10 digit secondary  mobile number")
	private String secondaryMobile;

	@NotNull
	private String dateOfBirth;
	@NotNull
	private char gender;

	/*
	 * @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy =
	 * "user")
	 * 
	 * @JsonManagedReference private List<Account> accounts =new ArrayList<>();
	 */
	private long accountId;

	public User() {

	}

}
