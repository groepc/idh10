package edu.avans.hartigehap.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true, of = { "menuItem", "quantity" })
@NoArgsConstructor
public abstract class BaseOrderItem extends DomainObject {
	private static final long serialVersionUID = 1L;

	// unidirectional many-to-one
	// deliberate: no cascade!!
	@ManyToOne
	private MenuItem menuItem;

	private int quantity = 0;
	
	private Double price = 0.0;

	public BaseOrderItem(MenuItem menuItem, int quantity, Double price) {
		this.menuItem = menuItem;
		this.quantity = quantity;
		this.price = price;
	}

	/* business logic */

	public void incrementQuantity() {
		quantity++;
	}

	public void decrementQuantity() {
		assert quantity > 0 : "quantity cannot be below 0";
		quantity--;
	}

	@Transient
	public Double getPrice() {
		return menuItem.getPrice();
	}

	public String description() {
		return menuItem.getId() + " (" + quantity + "x)";
	}
}