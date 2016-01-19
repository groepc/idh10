package edu.avans.hartigehap.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeDiscountFactory {

	public static DateTimeDiscountTemplate  getDateTimeDiscount(Date dateTime) {
		int time = Integer.parseInt(new SimpleDateFormat("HH").format(dateTime));
		
		if (time <= 12) {
			return new MorningDiscount();
		} else {
			return new AfternoonDiscount();
		}
	}
}
