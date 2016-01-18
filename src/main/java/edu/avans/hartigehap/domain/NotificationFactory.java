package edu.avans.hartigehap.domain;

public class NotificationFactory {
	public static NotificationAdapter getNotification(String type) {

		if (type == null) {
			return null;
		}

		if (type.equalsIgnoreCase("sms")) {
			return new SmsNotificationAdapter();
		} else if (type.equalsIgnoreCase("email")) {
			return new EmailNotificationAdapter(new EmailNotificationImpl());
		}

		return null;
	}
}
