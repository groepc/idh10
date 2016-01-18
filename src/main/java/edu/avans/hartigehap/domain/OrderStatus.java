package edu.avans.hartigehap.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.Setter;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id") 
@Getter @Setter

public abstract class OrderStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum OrderStatusId {
		CREATED, SUBMITTED, PLANNED, PREPARED, SERVED
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	protected  OrderStatusId orderStatusId;

	@OneToOne(cascade = javax.persistence.CascadeType.ALL, mappedBy="orderStatus")Order order;
	
	public OrderStatus(Order order){
		this.order = order;
	}
	
	public OrderStatus(){
	}
	
	// business methods
	
	public abstract void submit() throws StateException;
	public abstract void plan() throws StateException;
	public abstract void prepare() throws StateException;
	public abstract void serve() throws StateException;

}