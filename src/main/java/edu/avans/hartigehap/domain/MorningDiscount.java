package edu.avans.hartigehap.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MorningDiscount extends DateTimeDiscountTemplate {

	@Override
	protected double calculateDiscountPercentage(double price, Date dateTime) {
		String time = new SimpleDateFormat("HH").format(dateTime);
		switch (time) {
		case "07":
			return 0.97;
		case "10":
			return 0.90;
		case "11":
			return 0.98;
		default:
			return 0;
		}
	}

}
