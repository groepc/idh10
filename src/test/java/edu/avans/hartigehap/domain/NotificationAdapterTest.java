package edu.avans.hartigehap.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class NotificationAdapterTest {
	
	@Test
	public void testAutowire() {
		
		NotificationAdapter notifAdapter = NotificationFactory.getNotification("sms");
		
		Boolean status = notifAdapter.request("06 10 01 10 10", "Hallo wereld!");
		
		assertEquals(status, true);
	}

}
