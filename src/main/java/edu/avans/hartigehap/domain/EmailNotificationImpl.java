package edu.avans.hartigehap.domain;

public class EmailNotificationImpl {

	public void doMakeEmailRequest(String address, String content) {
		System.out.println("Sending email notification to " + address + " with message " + content);
	}
	
}
