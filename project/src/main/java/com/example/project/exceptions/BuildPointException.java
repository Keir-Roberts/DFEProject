package com.example.project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Too many 'build points' have been used in this monster")
public class BuildPointException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5615972495964470436L;

	public BuildPointException(String errorMessage) {
		super(errorMessage);
	}
	
	
}
