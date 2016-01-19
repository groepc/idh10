package edu.avans.hartigehap.web.controller;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.avans.hartigehap.domain.BaseOrderItem;
import edu.avans.hartigehap.domain.Bill;
import edu.avans.hartigehap.domain.Order;
import edu.avans.hartigehap.domain.Restaurant;
import edu.avans.hartigehap.domain.Order.OrderType;
import edu.avans.hartigehap.service.OrderService;
import edu.avans.hartigehap.service.RestaurantService;
import edu.avans.hartigehap.web.form.Message;

@Controller
@PreAuthorize("hasRole('ROLE_DELIVERER')")
public class DelivererController {

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/restaurants/{restaurantName}/deliverer", method = RequestMethod.GET)
	public String showDeliverer(@PathVariable("restaurantName") String restaurantName, Model uiModel) {

		// warmup stuff
		Collection<Restaurant> restaurants = restaurantService.findAll();
		uiModel.addAttribute("restaurants", restaurants);
		Restaurant restaurant = restaurantService.fetchWarmedUp(restaurantName);
		uiModel.addAttribute("restaurant", restaurant);
		
		List<Order> allOnlinePreparedOrders = orderService.findPreparedOrdersForRestaurant(OrderType.ONLINE,restaurant);
		uiModel.addAttribute("allOnlinePreparedOrders", allOnlinePreparedOrders);

		return "hartigehap/deliverer";
	}
	
	@RequestMapping(value = "/deliverer/orders/{orderId}", method = RequestMethod.GET)
	public String showOrderInDeliverer(@PathVariable("orderId") String orderId, Model uiModel, Locale locale) {

		// warmup stuff
		Order order = warmupRestaurantByOrder(orderId, uiModel);
		Restaurant resto = order.getBill().getDiningTable().getRestaurant();

		List<Order> allOnlinePreparedOrders = orderService.findPreparedOrdersForRestaurant(OrderType.ONLINE,resto);
		uiModel.addAttribute("allOnlinePreparedOrders", allOnlinePreparedOrders);

		String orderContent = "";
		for (BaseOrderItem orderItem : order.getOrderItems()) {
			orderContent += orderItem.getMenuItem().getId() + " (" + orderItem.getQuantity() + "x)" + "; ";
		}

		uiModel.addAttribute("message", new Message("info",
				messageSource.getMessage("label_order_content", new Object[] {}, locale) + ": " + orderContent));

		return "hartigehap/deliverer";
	}

	private Order warmupRestaurantByOrder(String orderId, Model uiModel) {
		Order order = orderService.findById(Long.valueOf(orderId));
		Collection<Restaurant> restaurants = restaurantService.findAll();
		uiModel.addAttribute("restaurants", restaurants);
		Restaurant restaurant = restaurantService
				.fetchWarmedUp(order.getBill().getDiningTable().getRestaurant().getId());
		uiModel.addAttribute("restaurant", restaurant);
		return order;
	}
	
}
