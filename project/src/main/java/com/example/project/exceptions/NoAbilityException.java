package com.example.project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Could not find a matching type")
public class NoAbilityException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5012375364496315779L;

	public NoAbilityException(String message) {
		super(message);
	}

	
}
