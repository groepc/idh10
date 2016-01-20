package edu.avans.hartigehap.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FoodDecoratorTest {

	@Test
	public void create() {

		// set-up base food
		BaseFood base = new BaseFood("Pizza Margharita", 7.0);
		BaseFood extraCheese = new Ingredient("Extra cheese", 1.0, base);
		BaseFood extraOnion = new Ingredient("Extra onion", 1.1, extraCheese);

		assertEquals(extraOnion.getPrice(), new Double(9.1));
	}
}
