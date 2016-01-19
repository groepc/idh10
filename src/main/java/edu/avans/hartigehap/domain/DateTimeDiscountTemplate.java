package edu.avans.hartigehap.domain;

import java.util.Date;

abstract public class DateTimeDiscountTemplate {

	public double getPercentage(double price, Date dateTime) {
		return this.calculateDiscountPercentage(price, dateTime);
	}
	
	protected abstract double calculateDiscountPercentage (double price, Date dateTime);

}
