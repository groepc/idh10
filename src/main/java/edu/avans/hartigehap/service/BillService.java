package edu.avans.hartigehap.service;

import java.util.List;

import edu.avans.hartigehap.domain.BaseOrderItem;
import edu.avans.hartigehap.domain.Bill;
import edu.avans.hartigehap.domain.DiningTable;
import edu.avans.hartigehap.domain.MenuItem;
import edu.avans.hartigehap.domain.OrderItem;
import edu.avans.hartigehap.domain.Restaurant;
import edu.avans.hartigehap.domain.StateException;

public interface BillService {
	Bill findById(Long billId);

	Bill save(Bill bill);

	void billHasBeenPaid(Bill bill) throws StateException;

	List<Bill> findSubmittedBillsForRestaurant(Restaurant restaurant);

	OrderItem addOrderItemOnline(Long billId, String menuItemName);

	BaseOrderItem addOrderOptionOnline(Long billId, BaseOrderItem orderItem, String orderOption);
}
