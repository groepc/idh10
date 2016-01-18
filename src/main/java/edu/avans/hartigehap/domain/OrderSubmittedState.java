package edu.avans.hartigehap.domain;

import java.util.Date;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

public class OrderSubmittedState extends OrderStatus {

		private static final long serialVersionUID = 1L;
		
		public OrderSubmittedState(Order order){
			super(order);
			orderStatusId = OrderStatusId.SUBMITTED;
		}
		
		public OrderSubmittedState(){
			orderStatusId = OrderStatusId.SUBMITTED;
		}

		@Override
		public void submit() throws StateException{
			throw new StateException(
				"not allowed to submit an order, if it is not in the created state");		
			
		}

		@Override
		public void plan() throws StateException{
			order.setPlannedTime(new Date());
			order.setOrderStatus(new OrderSubmittedState(getOrder()));	
		}

		@Override
		public void prepare() throws StateException {
			throw new StateException(
					"not allowed to prepare an order, if it is not in the planned state");
		}

		
		@Override
		public void serve() throws StateException {
			throw new StateException(
					"not allowed to serve an order, if it is not in the prepared state");
		}
	}