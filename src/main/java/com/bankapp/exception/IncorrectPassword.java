package com.bankapp.exception;

@SuppressWarnings("serial")
public class IncorrectPassword extends RuntimeException {

	public IncorrectPassword(String message) {
		super(message);
	}

}
