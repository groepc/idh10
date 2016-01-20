package edu.avans.hartigehap.domain;

public class SmsNotificationAdapter implements NotificationAdapter {

	/**
	 * Singleton pattern
	 */
	private SmsNotificationImpl smsNotificationImpl = SmsNotificationImpl.getInstance();

	@Override
	public Boolean request(String receiver, String body) {
		return smsNotificationImpl.createSmsNotification(receiver, body);
	}
}
