package edu.avans.hartigehap.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import edu.avans.hartigehap.domain.Order;
import edu.avans.hartigehap.domain.OrderStatus;
import edu.avans.hartigehap.domain.Restaurant;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long>, OrderRepositoryCustom {

	List<Order> findByOrderStatusOrderStatusIdAndBillDiningTableRestaurant(
			OrderStatus.OrderStatusId orderStatus, Restaurant restaurant, Sort sort);
	
}
