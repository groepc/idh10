package edu.avans.hartigehap.domain;

import java.util.Date;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id") 
@Getter @Setter

public class OrderCreatedState extends OrderStatus {
private static final long serialVersionUID = 1L;
	
	public OrderCreatedState(Order order){
		super(order);
		orderStatusId = OrderStatusId.CREATED;
	}
	
	public OrderCreatedState(){
		orderStatusId = OrderStatusId.CREATED;
	}

	@Override
	public void submit() throws StateException{
		if (order.isEmpty()) {
			throw new StateException("not allowed to submit an empty order");
		}
		order.setSubmittedTime(new Date());
		order.setOrderStatus(new OrderSubmittedState(getOrder()));			
	}

	@Override
	public void plan()throws StateException{
		throw new StateException(
			"not allowed to plan an order, if it is not in the submitted state");		
	}

	@Override
	public void prepare() throws StateException{
		throw new StateException(
			"not allowed to prepare an order, if it is not in the planned state");
	}


	@Override
	public void serve() throws StateException{
		throw new StateException(
				"not allowed to serve an order, if it is not in the prepared state");
	}
	
}
