package com.bankapp.exception;

@SuppressWarnings("serial")
public class AddressNotFound extends RuntimeException {

	public AddressNotFound(String message) {
		super(message);
	}

}
