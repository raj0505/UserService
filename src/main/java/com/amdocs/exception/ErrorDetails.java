package com.amdocs.exception;

import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class ErrorDetails extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date errortime;
	private String errorCode;
	private String errorMessage;
	
	public ErrorDetails() {
		
	}

	public ErrorDetails(Date errortime, String errorCode, String errorMessage) {
		super();
		this.errortime = errortime;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	
}
