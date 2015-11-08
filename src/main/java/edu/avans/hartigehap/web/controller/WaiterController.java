package edu.avans.hartigehap.web.controller;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import edu.avans.hartigehap.domain.*;
import edu.avans.hartigehap.service.*;
import edu.avans.hartigehap.web.form.Message;

@Controller
@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
@Slf4j
public class WaiterController {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private BillService billService;
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/restaurants/{restaurantName}/waiter", method = RequestMethod.GET)
    public String showWaiter(@PathVariable("restaurantName") String restaurantName, Model uiModel) {

        // warmup stuff
        Collection<Restaurant> restaurants = restaurantService.findAll();
        uiModel.addAttribute("restaurants", restaurants);
        Restaurant restaurant = restaurantService.fetchWarmedUp(restaurantName);
        uiModel.addAttribute("restaurant", restaurant);

        List<Order> allPreparedOrders = orderService.findPreparedOrdersForRestaurant(restaurant);
        uiModel.addAttribute("allPreparedOrders", allPreparedOrders);

        List<Bill> allSubmittedBills = billService.findSubmittedBillsForRestaurant(restaurant);
        uiModel.addAttribute("allSubmittedBills", allSubmittedBills);

        return "hartigehap/waiter";
    }

    @RequestMapping(value = "/waiter/orders/{orderId}", method = RequestMethod.GET)
    public String showOrderInWaiter(@PathVariable("orderId") String orderId, Model uiModel, Locale locale) {

        // warmup stuff
        Order order = warmupRestaurantByOrder(orderId, uiModel);
        Restaurant resto = order.getBill().getDiningTable().getRestaurant();

        List<Order> allPreparedOrders = orderService.findPreparedOrdersForRestaurant(resto);
        uiModel.addAttribute("allPreparedOrders", allPreparedOrders);

        List<Bill> allSubmittedBills = billService.findSubmittedBillsForRestaurant(resto);
        uiModel.addAttribute("allSubmittedBills", allSubmittedBills);

        String orderContent = "";
        for (OrderItem orderItem : order.getOrderItems()) {
            orderContent += orderItem.getMenuItem().getId() + " (" + orderItem.getQuantity() + "x)" + "; ";
        }

        uiModel.addAttribute("message", new Message("info",
                messageSource.getMessage("label_order_content", new Object[] {}, locale) + ": " + orderContent));

        return "hartigehap/waiter";
    }

    @RequestMapping(value = "/waiter/bills/{billId}", method = RequestMethod.GET)
    public String showBillInWaiter(@PathVariable("billId") String billId, Model uiModel, Locale locale) {

        // warmup stuff
        Bill bill = warmupRestaurant(billId, uiModel);
        Restaurant resto = bill.getDiningTable().getRestaurant();

        List<Order> allPreparedOrders = orderService.findPreparedOrdersForRestaurant(resto);
        uiModel.addAttribute("allPreparedOrders", allPreparedOrders);

        List<Bill> allSubmittedBills = billService.findSubmittedBillsForRestaurant(resto);
        uiModel.addAttribute("allSubmittedBills", allSubmittedBills);

        uiModel.addAttribute("message",
                new Message("info",
                        messageSource.getMessage("label_bill_amount", new Object[] {}, locale) + ": "
                                + bill.getPriceAllOrders() + " "
                                + messageSource.getMessage("label_currency", new Object[] {}, locale)));

        return "hartigehap/waiter";
    }

    @RequestMapping(value = "/waiter/orders/{orderId}", method = RequestMethod.PUT)
    public String receiveOrderEvent(@PathVariable("orderId") String orderId, @RequestParam String event,
            Model uiModel) {

        Order order = warmupRestaurantByOrder(orderId, uiModel);

        switch (event) {
        case "orderHasBeenServed":
            orderHasBeenServed(order);
            break;
            
        default:
            log.error("Internal error: event " + event + " not recognized");
            break;
        }
        
        return "redirect:/restaurants/" + order.getBill().getDiningTable().getRestaurant().getId() + "/waiter";
    }

    private void orderHasBeenServed(Order order) {
        try {
            orderService.orderServed(order);
        } catch (StateException e) {
            log.error("Internal error has occurred! Order " + Long.valueOf(order.getId())
                    + "has not been changed to served state!", e);
        }
    }
    

    @RequestMapping(value = "/waiter/bills/{billId}", method = RequestMethod.PUT)
    public String receiveBillEvent(@PathVariable("billId") String billId, @RequestParam String event, Model uiModel) {

        Bill bill = warmupRestaurant(billId, uiModel);

        switch (event) {
        case "billHasBeenPaid":
            billHasBeenPaid(bill);
            break;

        default:
            log.error("Internal error: event " + event + " not recognized");
            break;
        }

        return "redirect:/restaurants/" + bill.getDiningTable().getRestaurant().getId() + "/waiter";
    }
    
    private void billHasBeenPaid(Bill bill) {
        try {
            billService.billHasBeenPaid(bill);
        } catch (StateException e) {
            log.error("Internal error has occurred! Order " + Long.valueOf(bill.getId())
                    + "has not been changed to served state!", e);
        }
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

    private Bill warmupRestaurant(String billId, Model uiModel) {
        Bill bill = billService.findById(Long.valueOf(billId));
        Collection<Restaurant> restaurants = restaurantService.findAll();
        uiModel.addAttribute("restaurants", restaurants);
        Restaurant restaurant = restaurantService.fetchWarmedUp(bill.getDiningTable().getRestaurant().getId());
        uiModel.addAttribute("restaurant", restaurant);
        return bill;
    }
}
