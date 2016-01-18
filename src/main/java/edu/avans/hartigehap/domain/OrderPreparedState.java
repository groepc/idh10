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

public class OrderPreparedState extends OrderStatus {
	
	private static final long serialVersionUID = 1L;

	public OrderPreparedState(Order order){
		super(order);
		orderStatusId = OrderStatusId.PREPARED;
	}
	
	public OrderPreparedState(){
		orderStatusId = OrderStatusId.PREPARED;
	}

	@Override
	public void submit() throws StateException {
		throw new StateException(
			"not allowed to submit an order, if it is not in the created state");		
	}

	@Override
	public void plan() throws StateException {
		throw new StateException(
			"not allowed to plan an order, if it is not in the submitted state");		
	}

	@Override
	public void prepare() throws StateException {
		throw new StateException(
				"not allowed to prepare an order, if it is not in the planned state");
	}

	@Override
	public void serve() throws StateException {
		order.setServedTime(new Date());
		order.setOrderStatus(new OrderServedState(getOrder()));		
	}

}