package edu.avans.hartigehap.domain;

import org.springframework.beans.factory.annotation.Autowired;

public class EmailNotificationAdapter implements NotificationAdapter {
	
	@Autowired
	private EmailNotificationImpl emailNotificationImpl;

	@Override
	public void request(String receiver, String body) {
		emailNotificationImpl.doMakeEmailRequest(receiver, body);
	}

}
