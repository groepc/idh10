package edu.avans.hartigehap.domain;

import org.springframework.beans.factory.annotation.Autowired;

public class SmsNotificationAdapter implements NotificationAdapter {
	
	/**
	 * Singleton pattern
	 */
	@Autowired
	private SmsNotificationImpl smsNotificationImpl = SmsNotificationImpl.getInstance();

	@Override
	public void request(String receiver, String body) {
		smsNotificationImpl.createSmsNotification(receiver, body);
	}
}
