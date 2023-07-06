package com.bankapp.exception;

@SuppressWarnings("serial")
public class BadRequest extends RuntimeException {

	public BadRequest(String message) {
		super(message);
	}

}
