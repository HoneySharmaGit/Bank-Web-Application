package com.bankapp.exception;

@SuppressWarnings("serial")
public class AdminNotFound extends RuntimeException {

	public AdminNotFound(String message) {
		super(message);
	}

}
