package com.amdocs.exception;

import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class EmptyInputException extends RuntimeException {
	
	private Date errortime;
	private String errorCode;
	private String errorMessage;
	
	public EmptyInputException() {
		
	}

}
