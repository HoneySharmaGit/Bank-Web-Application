package com.bankapp.exception;

@SuppressWarnings("serial")
public class UserExists extends RuntimeException {

	public UserExists(String message) {
		super(message);
	}

}
