package edu.avans.hartigehap.domain;

public class Ingredient extends FoodDecorator {
	private static final long serialVersionUID = 1L;
	
	private Food food;
	
	private String name;
	private Double price;

	public Ingredient(String name, Double price, Food food) {
		this.food = food;
		this.name = name;
		this.price = price;
	}
	
	@Override
	public Double getPrice() {
		return food.getPrice() + this.price;
	}

}
