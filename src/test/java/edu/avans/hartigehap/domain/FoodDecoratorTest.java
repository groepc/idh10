package edu.avans.hartigehap.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FoodDecoratorTest {

	@Test
    public void create() {
		
		// set-up base food
		Food base = new Food("Pizza Margharita", 7.0);
		Food extraCheese = new Ingredient("Extra cheese", 1.0, base);
		Food extraOnion = new Ingredient("Extra onion", 1.1, extraCheese);
		
		assertEquals(extraOnion.getPrice(), new Double(9.1));
    }
}
