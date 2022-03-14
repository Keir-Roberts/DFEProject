package com.example.project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "This monster already has this ability")
public class existingAbilityException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2034682142253726011L;

	public existingAbilityException(String message) {
		super(message);
	}

}
