package edu.avans.hartigehap.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "FOOD")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@Getter
@Setter
@NoArgsConstructor
public class BaseFood extends DomainObject {
	private static final long serialVersionUID = 1L;

	private String name = "";
	private Double price = 0.0;

	public BaseFood(String name, Double price) {
		this.name = name;
		this.price = price;
	}
}
