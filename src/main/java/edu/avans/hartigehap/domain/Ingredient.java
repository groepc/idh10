package edu.avans.hartigehap.domain;

public class Ingredient extends FoodDecorator {
	private static final long serialVersionUID = 1L;
	
	private BaseFood baseFood;
	
	private String name;
	private Double price;

	public Ingredient(String name, Double price, BaseFood baseFood) {
		this.baseFood = baseFood;
		this.name = name;
		this.price = price;
	}
	
	@Override
	public Double getPrice() {
		return baseFood.getPrice() + this.price;
	}

}
