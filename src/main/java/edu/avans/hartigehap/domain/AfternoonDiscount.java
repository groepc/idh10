package edu.avans.hartigehap.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AfternoonDiscount extends DateTimeDiscountTemplate {

	@Override
	protected double calculateDiscountPercentage(double price, Date dateTime) {
		String time = new SimpleDateFormat("HH").format(dateTime);
		if (price > 10.00) {
			switch (time) {
			case "18":
				return 0.95;
			case "19":
				return 0.60;
			case "21":
				return 0.99;
			}
		}
		return 1;
	}

}
