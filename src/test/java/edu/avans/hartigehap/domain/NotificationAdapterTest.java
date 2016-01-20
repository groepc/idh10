package edu.avans.hartigehap.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NotificationAdapterTest {

	@Test
	public void testEmailNotificationFactory() {
		assertEquals(NotificationFactory.getNotification(""), null);
	}

	@Test
	public void testEmailNotification() {
		NotificationAdapter notifAdapter = NotificationFactory.getNotification("email");

		Boolean status = notifAdapter.request("06 10 01 10 10", "Hallo wereld!");

		assertEquals(status, true);
	}

}
