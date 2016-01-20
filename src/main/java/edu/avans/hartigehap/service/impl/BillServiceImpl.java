package edu.avans.hartigehap.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.avans.hartigehap.domain.BaseOrderItem;
import edu.avans.hartigehap.domain.Bill;
import edu.avans.hartigehap.domain.MenuItem;
import edu.avans.hartigehap.domain.Order;
import edu.avans.hartigehap.domain.OrderItem;
import edu.avans.hartigehap.domain.Restaurant;
import edu.avans.hartigehap.domain.StateException;
import edu.avans.hartigehap.repository.BillRepository;
import edu.avans.hartigehap.repository.MenuItemRepository;
import edu.avans.hartigehap.repository.OrderRepository;
import edu.avans.hartigehap.service.BillService;

@Service("billService")
@Repository
@Transactional(rollbackFor = StateException.class)
public class BillServiceImpl implements BillService {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private BillRepository billRepository;
	@Autowired
	private MenuItemRepository menuItemRepository;

	@Transactional(readOnly = true)
	public Bill findById(Long billId) {
		return billRepository.findOne(billId);
	}

	public void billHasBeenPaid(Bill bill) throws StateException {
		bill.paid();
	}

	@Transactional(readOnly = true)
	public List<Bill> findSubmittedBillsForRestaurant(Restaurant restaurant) {
		// a query created using a repository method name
		return billRepository.findByBillStatusAndDiningTableRestaurant(Bill.BillStatus.SUBMITTED, restaurant,
				new Sort(Sort.Direction.ASC, "submittedTime"));
	}

	@Override
	public Bill save(Bill bill) {
		return billRepository.save(bill);
	}

	@Override
	public OrderItem addOrderItemOnline(Long billId, String menuItemName) {
		MenuItem menuItem = menuItemRepository.findOne(menuItemName);
		Bill currentBill = this.findById(billId);
		Order order = currentBill.getCurrentOrder();
		OrderItem orderItem = order.addOnlineOrderItem(menuItem);
		
		//order.setOrderStatus(OrderStatus.CREATED);

		return orderItem;
	}

	public BaseOrderItem addOrderOptionOnline(Long billId, BaseOrderItem orderItem, String orderOption) {
		MenuItem menuItem = menuItemRepository.findOne(orderOption);

		Bill currentBill = this.findById(billId);
		Order order = currentBill.getCurrentOrder();
		BaseOrderItem orderOptionReturned = order.addOnlineOrderOption(orderItem, menuItem);
		
		//order.setOrderStatus(OrderStatus.CREATED);
		
		return orderOptionReturned;

	}
}
