package edu.avans.hartigehap.domain;

import javax.persistence.Entity;

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
@ToString(callSuper = true, includeFieldNames = true, of = {})
@NoArgsConstructor
public class Drink extends MenuItem {
	private static final long serialVersionUID = 1L;

	private Size size;

	public enum Size {
		SMALL, MEDIUM, LARGE
	}

	public Drink(String id, String imageFileName, int price, String recipe, Size size) {
		super(id, imageFileName, price, recipe);
		this.size = size;

	}

	// business logic
}
