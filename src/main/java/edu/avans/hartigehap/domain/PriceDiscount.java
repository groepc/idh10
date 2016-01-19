package edu.avans.hartigehap.domain;

public class PriceDiscount implements DiscountInterface {

	@Override
	public double getNewPrice(double price) {
		double percentage = 1;
		if (price > 50.00) {
		   percentage = 0.79;	
		} else if (price > 20.00) {
			percentage = 0.89;
		} else if (price > 10) {
			percentage = 0.99;
		}
		
		
		return price * percentage;
	}

}
