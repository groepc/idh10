package edu.avans.hartigehap.domain;

public class EmailNotificationAdapter implements NotificationAdapter {

	private EmailNotificationImpl emailNotificationImpl;

	public EmailNotificationAdapter(EmailNotificationImpl emailNotificationImpl) {
		this.emailNotificationImpl = emailNotificationImpl;
	}

	@Override
	public Boolean request(String receiver, String body) {
		return emailNotificationImpl.doMakeEmailRequest(receiver, body);
	}

}
