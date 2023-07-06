package com.bankapp.helper;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsHelper {

	@Value("${twilio.account.sid}")
	private String ACCOUNT_SID;

	@Value("${twilio.auth.token}")
	private String AUTH_TOKEN;

	@Value("${twilio.phone.number}")
	private String TWILIO_PHONE_NUMBER;

	public void sendSms(String recipientPhoneNumber, String otpCode) {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		Message message = Message.creator(new PhoneNumber(recipientPhoneNumber), new PhoneNumber(TWILIO_PHONE_NUMBER),
				"Your OTP(One Time Password) for verification is: " + otpCode).create();
		System.out.println("SMS sent with message SID: " + message.getSid());
	}

	public int randomOtpForNumber() {
		Random random = new Random();
		int otp = 100000 + random.nextInt(899999);
		System.out.println("generated otp is: " + otp);
		return otp;
	}

}
