package com.example.project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Could not find a matching type")
public class NoTypeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8247069465113567553L;

	public NoTypeException(String errorMessage) {
		super(errorMessage);
	}
}
