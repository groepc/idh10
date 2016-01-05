package edu.avans.hartigehap.domain;

import org.springframework.beans.factory.annotation.Autowired;

public class EmailNotificationAdapter implements NotificationAdapter {
	
	@Autowired
	private EmailNotificationImpl emailNotificationImpl;

	@Override
	public Boolean request(String receiver, String body) {
		return emailNotificationImpl.doMakeEmailRequest(receiver, body);
	}

}
