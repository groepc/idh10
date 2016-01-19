package edu.avans.hartigehap.domain;

public class DateTimeDiscountFactory {

	public static DateTimeDiscountTemplate  getDateTimeDiscount(String type) {

		if (type == null) {
			return null;
		}

		if (type.equalsIgnoreCase("morning")) {
			return new MorningDiscount();
		} else if (type.equalsIgnoreCase("afternoon")) {
			return new AfternoonDiscount();
		}

		return null;
	}
}
