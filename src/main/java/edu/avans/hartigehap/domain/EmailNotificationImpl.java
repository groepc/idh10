package edu.avans.hartigehap.domain;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class EmailNotificationImpl {
	
	public Boolean doMakeEmailRequest(String address, String content) {
		System.out.println("Sending email notification to " + address + " with message " + content);
		
		return true;
	}
	
}
