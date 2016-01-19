package edu.avans.hartigehap.domain;

import java.util.Date;

abstract public class DateTimeDiscountTemplate {

	public double calculateNewPrice(double price, Date dateTime) {
		Double percentage = this.calculateDiscountPercentage(price, dateTime);
		return price * percentage;
	}
	protected abstract double calculateDiscountPercentage (double price, Date dateTime);

}
