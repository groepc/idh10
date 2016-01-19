package edu.avans.hartigehap.service;

import java.util.List;

import edu.avans.hartigehap.domain.Order;
import edu.avans.hartigehap.domain.Restaurant;
import edu.avans.hartigehap.domain.StateException;
import edu.avans.hartigehap.domain.Order.OrderType;

public interface OrderService {
	Order findById(Long orderId);
	
	List<Order> findSubmittedOrdersForRestaurant(Order.OrderType orderType, Restaurant restaurant);
	
	List<Order> findPlannedOrdersForRestaurant(Restaurant restaurant);

	List<Order> findPreparedOrdersForRestaurant(Restaurant restaurant);

	void planOrder(Order order) throws StateException;

	void orderPrepared(Order order) throws StateException;

	void orderServed(Order order) throws StateException;

}
