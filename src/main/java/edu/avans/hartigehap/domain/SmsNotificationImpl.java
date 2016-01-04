package edu.avans.hartigehap.domain;

public class SmsNotificationImpl {
	
	/**
	 * Singleton pattern in action
	 * We use a singleton, because we only want 1 SMS notification implementation
	 */
	private static SmsNotificationImpl instance = new SmsNotificationImpl();
	
	private SmsNotificationImpl() {}
	
	public static SmsNotificationImpl getInstance(){
		return instance;
	}
	
	public void createSmsNotification(String number, String content) {
		System.out.println("Sending SMS notification to " + number + " with message " + content);
	}
}
