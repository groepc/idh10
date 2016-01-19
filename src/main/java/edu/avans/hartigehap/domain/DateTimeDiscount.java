package edu.avans.hartigehap.domain;

import java.sql.Date;

public class DateTimeDiscount implements DiscountInterface {

	@Override
	public double getPercentage(double price) {
		
		Date dateTime = new Date(System.currentTimeMillis());
		DateTimeDiscountTemplate discount = DateTimeDiscountFactory.getDateTimeDiscount(dateTime);
		return discount.getPercentage(price, dateTime);
		
	}

}
