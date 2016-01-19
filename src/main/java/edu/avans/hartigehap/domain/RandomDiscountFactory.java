package edu.avans.hartigehap.domain;

import java.util.Random;

public class RandomDiscountFactory {
	
	public static DiscountInterface getRandomDiscount () {
		
		Random random = new Random();
		if (random.nextBoolean()) {
			return new DateTimeDiscount();
		} 
		
		return new PriceDiscount();
	}

}
