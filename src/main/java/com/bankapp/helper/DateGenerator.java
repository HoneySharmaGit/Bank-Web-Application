package com.bankapp.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class DateGenerator {

	public String getCurrentTime() {
		DateTimeFormatter datetimeformatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");
		LocalDateTime now = LocalDateTime.now();
		String date = datetimeformatter.format(now);
		return date;
	}

}
