package com.amdocs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(description = "Details about Account")
@Data
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	@Column(length = 5)
	private long accountId;

	@NotNull
	@Size(min = 4, max = 40, message = "Branch name Should have at least 4 characters")

	private String branchName;

	@NotNull
	@Size(min = 4, max = 40, message = "Savings-S | Current-C")
	private String accountType;

	@NotNull

	private double accountBalance;

	/*
	 * @ManyToOne(fetch = FetchType.LAZY) private User user;
	 */
	private long userId;

	public Account() {

	}

}
