package edu.avans.hartigehap.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.avans.hartigehap.domain.BaseFood;
import edu.avans.hartigehap.domain.BaseOrderItem;
import edu.avans.hartigehap.domain.Bill;
import edu.avans.hartigehap.domain.Customer;
import edu.avans.hartigehap.domain.DiningTable;
import edu.avans.hartigehap.domain.Meal;
import edu.avans.hartigehap.domain.MealOption;
import edu.avans.hartigehap.domain.MenuItem;
import edu.avans.hartigehap.domain.NotificationAdapter;
import edu.avans.hartigehap.domain.NotificationFactory;
import edu.avans.hartigehap.domain.OrderItem;
import edu.avans.hartigehap.domain.Restaurant;
import edu.avans.hartigehap.repository.MealOptionRepository;
import edu.avans.hartigehap.repository.MenuItemRepository;
import edu.avans.hartigehap.service.BaseFoodService;
import edu.avans.hartigehap.service.BillService;
import edu.avans.hartigehap.service.CustomerService;
import edu.avans.hartigehap.service.DiningTableService;
import edu.avans.hartigehap.service.RestaurantService;
import edu.avans.hartigehap.web.form.Message;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class OnlineOrderController {

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private BaseFoodService baseFoodService;
	@Autowired
	private MealOptionRepository mealOptionRepository;
	@Autowired
	private DiningTableService diningTableService;
	@Autowired
	private BillService billService;

	/**
	 * STEP 1
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/online-order", "/online-order/customer-details" }, method = RequestMethod.GET)
	public String onlineOrderCustomerDetails(Model uiModel) {
		log.info("Online order step 1, customer details");
		Customer customer = new Customer();
		uiModel.addAttribute("customer", customer);
		return "hartigehap/onlineorder/customer-details";
	}

	/**
	 * STEP 1 process request
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/online-order", "/online-order/customer-details" }, method = RequestMethod.POST)
	public String onlineOrderCustomerDetailsProcess(@ModelAttribute @Valid Customer customer,
			BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest,
			RedirectAttributes redirectAttributes, Locale locale, HttpSession session) {

		System.out.println("Creating customer: " + customer.getFirstName() + " " + customer.getLastName());
		System.out.println("Binding Result target" + (Customer) bindingResult.getTarget());
		System.out.println("Binding Result: " + bindingResult);

		if (bindingResult.hasErrors()) {
			System.out.println(bindingResult.toString());
			uiModel.addAttribute("message",
					new Message("error", messageSource.getMessage("customer_save_fail", new Object[] {}, locale)));
			uiModel.addAttribute("customer", customer);
			return "hartigehap/onlineorder/customer-details";
		}

		// create customer
		Restaurant restaurant = restaurantService.fetchWarmedUp("HartigeHap");
		customer.setRestaurants(Arrays.asList(new Restaurant[] { restaurant }));
		customer = customerService.save(customer);

		// get dining table
		DiningTable table = diningTableService.findById(Long.parseLong("9999999"));

		// create bill
		Bill bill = new Bill();
		bill.setCustomer(customer);
		bill.setDiningTable(table);
		bill = billService.save(bill);

		log.info("Online order step 1, customer details Process");

		session.setAttribute("customerId", customer.getId());
		session.setAttribute("billId", bill.getId());

		return "redirect:/online-order/select-meals";
	}

	/**
	 * STEP 2
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/online-order/select-meals", method = RequestMethod.GET)
	public String onlineOrderSelectMeals(Model model, HttpSession session) {

		log.info("Online order step 2, select meals");

		if (session.getAttribute("customerId") == null) {
			return "redirect:/online-order";
		}

		Long billId = (Long) session.getAttribute("billId");

		// get existing items in this order
		Bill bill = billService.findById(billId);
		Collection<BaseOrderItem> items = bill.getCurrentOrder().getOrderItems();
		model.addAttribute("currentItems", items);

		// get foods (pizza's)
		Collection<MenuItem> menuItems = baseFoodService.findOnlineMenuItems();
		model.addAttribute("meals", menuItems);

		// get options
		Iterable<MealOption> mealOptions = mealOptionRepository.findAll();
		model.addAttribute("mealOptions", mealOptions);

		// get current customer email
		Long idCustomer = Long.parseLong(session.getAttribute("customerId").toString());
		Customer customer = customerService.findById(idCustomer);
		model.addAttribute("customerEmail", customer.getEmail());

		return "hartigehap/onlineorder/select-meals";
	}

	/**
	 * STEP 2 process pizza order
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/online-order/select-meals", method = RequestMethod.POST)
	public String onlineOrderSelectMealsProcess(Model uiModel, HttpServletRequest httpServletRequest,
			RedirectAttributes redirectAttributes, Locale locale, HttpSession session) {

		if (session.getAttribute("customerId") == null) {
			return "redirect:/online-order";
		}

		Long billId = (Long) session.getAttribute("billId");

		String menuItemName = httpServletRequest.getParameter("order");
		OrderItem orderItem = billService.addOrderItemOnline(billId, menuItemName);

		// get toppings
		String[] options = httpServletRequest.getParameterValues("options[]");

		for (String s : options) {
			billService.addOrderOptionOnline(billId, orderItem, s);
			System.out.println(s);
		}

		return "redirect:/online-order/select-meals";
	}

	/**
	 * STEP 3
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/online-order/payment", method = RequestMethod.GET)
	public String onlineOrderPayment(Model model) {
		log.info("Online order step 3, payment");
		return "hartigehap/onlineorder/payment";
	}

	/**
	 * STEP 4
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/online-order/receipt", method = RequestMethod.GET)
	public String onlineOrderReceipt(Model model) {

		NotificationAdapter notifier = NotificationFactory.getNotification("email");
		notifier.request("vadiemjanssens@gmail.com", "Hallo wereld!");

		log.info("Online order step 4, receipt");
		return "hartigehap/onlineorder/receipt";
	}

}