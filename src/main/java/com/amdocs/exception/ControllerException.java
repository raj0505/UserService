package com.amdocs.exception;

import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class ControllerException extends RuntimeException {
	
	private Date errortime;
	private String errorCode;
	private String errorMessage;
	
	public ControllerException() {
		
	}

}
