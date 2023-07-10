package com.bankapp.exception;

@SuppressWarnings("serial")
public class BankAccountExists extends RuntimeException {

	public BankAccountExists(String message) {
		super(message);
	}

}
