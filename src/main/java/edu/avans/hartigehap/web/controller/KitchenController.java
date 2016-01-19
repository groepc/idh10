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
import org.springframework.web.bind.annotation.RequestParam;

import edu.avans.hartigehap.domain.BaseOrderItem;
import edu.avans.hartigehap.domain.Order;
import edu.avans.hartigehap.domain.Order.OrderType;
import edu.avans.hartigehap.domain.Restaurant;
import edu.avans.hartigehap.domain.StateException;
import edu.avans.hartigehap.service.OrderService;
import edu.avans.hartigehap.service.RestaurantService;
import edu.avans.hartigehap.service.impl.OrderServiceImpl;
import edu.avans.hartigehap.web.form.Message;
import lombok.extern.slf4j.Slf4j;

@Controller
@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
@Slf4j
public class KitchenController {

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/restaurants/{restaurantName}/kitchen", method = RequestMethod.GET)
	public String showKitchen(@PathVariable("restaurantName") String restaurantName, Model uiModel) {

		// warmup stuff
		Collection<Restaurant> restaurants = restaurantService.findAll();
		uiModel.addAttribute("restaurants", restaurants);
		Restaurant restaurant = restaurantService.fetchWarmedUp(restaurantName);
		uiModel.addAttribute("restaurant", restaurant);
		
		List<Order> allOnlineSubmittedOrders = orderService.findSubmittedOrdersForRestaurant(OrderType.ONLINE, restaurant);
		uiModel.addAttribute("allOnlineSubmittedOrders", allOnlineSubmittedOrders);
		
		List<Order> allSubmittedOrders = orderService.findSubmittedOrdersForRestaurant(OrderType.RESTAURANT, restaurant);
		uiModel.addAttribute("allSubmittedOrders", allSubmittedOrders);

		List<Order> allPlannedOrders = orderService.findPlannedOrdersForRestaurant(restaurant);
		uiModel.addAttribute("allPlannedOrders", allPlannedOrders);

		return "hartigehap/kitchen";
	}

	@RequestMapping(value = "/kitchen/orders/{orderId}", method = RequestMethod.GET)
	public String showOrderInKitchen(@PathVariable("orderId") String orderId, Model uiModel, Locale locale) {

		// warmup stuff
		Order order = warmupRestaurant(orderId, uiModel);
		Restaurant resto = order.getBill().getDiningTable().getRestaurant();

		List<Order> allOnlineSubmittedOrders = orderService.findSubmittedOrdersForRestaurant(OrderType.ONLINE, resto);
		uiModel.addAttribute("allOnlineSubmittedOrders", allOnlineSubmittedOrders);
		
		List<Order> allSubmittedOrders = orderService.findSubmittedOrdersForRestaurant(OrderType.RESTAURANT, resto);
		uiModel.addAttribute("allSubmittedOrders", allSubmittedOrders);
	
		List<Order> allPlannedOrders = orderService.findPlannedOrdersForRestaurant(resto);
		uiModel.addAttribute("allPlannedOrders", allPlannedOrders);
		
		String orderContent = "";
		for (BaseOrderItem orderItem : order.getOrderItems()) {
			orderContent += orderItem.getMenuItem().getId() + " (" + orderItem.getQuantity() + "x)" + "; ";
		}

		uiModel.addAttribute("message", new Message("info",
				messageSource.getMessage("label_order_content", new Object[] {}, locale) + ": " + orderContent));

		return "hartigehap/kitchen";
	}

	@RequestMapping(value = "/kitchen/orders/{orderId}", method = RequestMethod.PUT)
	public String receiveOrderEvent(@PathVariable("orderId") String orderId, @RequestParam String event,
			Model uiModel) {

		Order order = warmupRestaurant(orderId, uiModel);

		switch (event) {
		case "planOrder":
			planOrder(order);
			break;

		case "orderHasBeenPrepared":
			orderHasBeenPrepared(order);
			break;

		default:
			log.error("Internal error: event " + event + " not recognized");
			break;
		}

		return "redirect:/restaurants/" + order.getBill().getDiningTable().getRestaurant().getId() + "/kitchen";
	}

	private void planOrder(Order order) {
		try {
			orderService.planOrder(order);
		} catch (StateException e) {
			log.error("Internal error has occurred! Order " + Long.valueOf(order.getId())
					+ "has not been changed to planned state!", e);
		}
	}

	private void orderHasBeenPrepared(Order order) {
		try {
			orderService.orderPrepared(order);
		} catch (StateException e) {
			log.error("Internal error has occurred! Order " + Long.valueOf(order.getId())
					+ "has not been changed to prepared state!", e);
		}
	}

	private Order warmupRestaurant(String orderId, Model uiModel) {
		Order order = orderService.findById(Long.valueOf(orderId));
		Collection<Restaurant> restaurants = restaurantService.findAll();
		uiModel.addAttribute("restaurants", restaurants);
		Restaurant restaurant = restaurantService
				.fetchWarmedUp(order.getBill().getDiningTable().getRestaurant().getId());
		uiModel.addAttribute("restaurant", restaurant);
		return order;
	}
}
