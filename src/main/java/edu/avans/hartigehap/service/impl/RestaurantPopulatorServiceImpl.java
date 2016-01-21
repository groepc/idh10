package edu.avans.hartigehap.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.avans.hartigehap.domain.BaseOrderItem;
import edu.avans.hartigehap.domain.Bill;
import edu.avans.hartigehap.domain.Bill.BillStatus;
import edu.avans.hartigehap.domain.Customer;
import edu.avans.hartigehap.domain.DiningTable;
import edu.avans.hartigehap.domain.Drink;
import edu.avans.hartigehap.domain.FoodCategory;
import edu.avans.hartigehap.domain.Meal;
import edu.avans.hartigehap.domain.MealOption;
import edu.avans.hartigehap.domain.Order;
import edu.avans.hartigehap.domain.Order.OrderType;
import edu.avans.hartigehap.domain.OrderSubmittedState;
import edu.avans.hartigehap.domain.Restaurant;
import edu.avans.hartigehap.repository.CustomerRepository;
import edu.avans.hartigehap.repository.FoodCategoryRepository;
import edu.avans.hartigehap.repository.MenuItemRepository;
import edu.avans.hartigehap.repository.RestaurantRepository;
import edu.avans.hartigehap.service.BillService;
import edu.avans.hartigehap.service.RestaurantPopulatorService;
import edu.avans.hartigehap.service.RestaurantService;

@Service("restaurantPopulatorService")
@Repository
@Transactional
public class RestaurantPopulatorServiceImpl implements RestaurantPopulatorService {
	final Logger logger = LoggerFactory.getLogger(RestaurantPopulatorServiceImpl.class);

	@Autowired
	private RestaurantRepository restaurantRepository;
	@Autowired
	private FoodCategoryRepository foodCategoryRepository;
	@Autowired
	private MenuItemRepository menuItemRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private BillService billService;
	@Autowired
	private RestaurantService restaurantService;

	private List<Meal> meals = new ArrayList<Meal>();
	private List<FoodCategory> foodCats = new ArrayList<FoodCategory>();
	private List<Drink> drinks = new ArrayList<Drink>();
	private List<MealOption> mealOptions = new ArrayList<MealOption>();
	private List<Customer> customers = new ArrayList<Customer>();

	/**
	 * menu items, food categories and customers are common to all restaurants
	 * and should be created only once. Although we can safely assume that the
	 * are related to at least one restaurant and therefore are saved via the
	 * restaurant, we save them explicitly anyway
	 */
	private void createCommonEntities() {

		/**
		 * @author Frans
		 * 
		 *         Create createFood
		 */

		createFoodCategory("low fat");
		createFoodCategory("high energy");
		createFoodCategory("vegatarian");
		createFoodCategory("italian");
		createFoodCategory("asian");
		createFoodCategory("alcoholic drinks");
		createFoodCategory("energizing drinks");
		createFoodCategory("online pizzas");

		createMeal("Pizza Hawaï", null, 7.0, "online",
				Arrays.<FoodCategory> asList(new FoodCategory[] { foodCats.get(7) }));
		createMeal("Pizza Shoarma", null, 10.0, "online",
				Arrays.<FoodCategory> asList(new FoodCategory[] { foodCats.get(7) }));
		createMeal("Pizza Salami", null, 8.50, "online",
				Arrays.<FoodCategory> asList(new FoodCategory[] { foodCats.get(7) }));
		createMeal("Pizza Margaritha", null, 6.25, "online",
				Arrays.<FoodCategory> asList(new FoodCategory[] { foodCats.get(7) }));
		createMeal("Pizza Funghi", null, 8.0, "online",
				Arrays.<FoodCategory> asList(new FoodCategory[] { foodCats.get(7) }));
		createMeal("Pizza Chicken Supreme", null, 9.0, "online",
				Arrays.<FoodCategory> asList(new FoodCategory[] { foodCats.get(7) }));

		createMeal("spaghetti", "spaghetti.jpg", 8.0, "easy",
				Arrays.<FoodCategory> asList(new FoodCategory[] { foodCats.get(3), foodCats.get(1) }));
		createMeal("macaroni", "macaroni.jpg", 8.0, "easy",
				Arrays.<FoodCategory> asList(new FoodCategory[] { foodCats.get(3), foodCats.get(1) }));
		createMeal("canneloni", "canneloni.jpg", 9.0, "easy",
				Arrays.<FoodCategory> asList(new FoodCategory[] { foodCats.get(3), foodCats.get(1) }));
		createMeal("pizza", "pizza.jpg", 9.0, "easy",
				Arrays.<FoodCategory> asList(new FoodCategory[] { foodCats.get(3), foodCats.get(1) }));
		createMeal("carpaccio", "carpaccio.jpg", 7.0, "easy",
				Arrays.<FoodCategory> asList(new FoodCategory[] { foodCats.get(3), foodCats.get(0) }));
		createMeal("ravioli", "ravioli.jpg", 8.0, "easy",
				Arrays.<FoodCategory> asList(new FoodCategory[] { foodCats.get(3), foodCats.get(1), foodCats.get(2) }));

		createMealOption("Extra kaas", "pizza.jpg", 0.25, "easy",
				Arrays.<FoodCategory> asList(new FoodCategory[] { foodCats.get(3), foodCats.get(2) }));
		createMealOption("Extra ui", "pizza.jpg", 0.35, "easy",
				Arrays.<FoodCategory> asList(new FoodCategory[] { foodCats.get(3), foodCats.get(2) }));
		createMealOption("Extra tomaat", "pizza.jpg", 0.15, "easy",
				Arrays.<FoodCategory> asList(new FoodCategory[] { foodCats.get(3), foodCats.get(2) }));
		createMealOption("Extra mozzarella", "pizza.jpg", 0.75, "easy",
				Arrays.<FoodCategory> asList(new FoodCategory[] { foodCats.get(3), foodCats.get(2) }));
		createMealOption("Extra ansjovis", "pizza.jpg", 1.0, "easy",
				Arrays.<FoodCategory> asList(new FoodCategory[] { foodCats.get(3), foodCats.get(2) }));

		createDrink("beer", "beer.jpg", 1.2, Drink.Size.LARGE,
				Arrays.<FoodCategory> asList(new FoodCategory[] { foodCats.get(5) }));
		createDrink("coffee", "coffee.jpg", 1.0, Drink.Size.MEDIUM,
				Arrays.<FoodCategory> asList(new FoodCategory[] { foodCats.get(6) }));

		byte[] photo = new byte[] { 127, -128, 0 };
		createCustomer("piet", "bakker", "test1@example.com", "1000AA", "10B", "", new DateTime(), 1, "description",
				photo);
		createCustomer("piet", "bakker", "test2@example.com", "2000BB", "10B", "", new DateTime(), 1, "description",
				photo);
		createCustomer("piet", "bakker", "test3@example.com", "3000CC", "10B", "", new DateTime(), 1, "description",
				photo);

	}

	private void createFoodCategory(String tag) {
		FoodCategory foodCategory = new FoodCategory(tag);
		foodCategory = foodCategoryRepository.save(foodCategory);
		foodCats.add(foodCategory);
	}

	private void createMeal(String name, String image, Double price, String recipe, List<FoodCategory> foodCats) {
		Meal meal = new Meal(name, image, price, recipe);
		// as there is no cascading between FoodCategory and MenuItem (both
		// ways), it is important to first
		// save foodCategory and menuItem before relating them to each other,
		// otherwise you get errors
		// like "object references an unsaved transient instance - save the
		// transient instance before flushing:"
		meal.addFoodCategories(foodCats);
		meal = menuItemRepository.save(meal);
		meals.add(meal);
	}

	private void createMealOption(String name, String image, Double price, String recipe, List<FoodCategory> foodCats) {
		MealOption mealOption = new MealOption(name, image, price, recipe);
		// as there is no cascading between FoodCategory and MenuItem (both
		// ways), it is important to first
		// save foodCategory and menuItem before relating them to each other,
		// otherwise you get errors
		// like "object references an unsaved transient instance - save the
		// transient instance before flushing:"
		mealOption.addFoodCategories(foodCats);
		mealOption = menuItemRepository.save(mealOption);
		mealOptions.add(mealOption);
	}

	private void createDrink(String name, String image, Double price, Drink.Size size, List<FoodCategory> foodCats) {
		Drink drink = new Drink(name, image, price, "", size);
		drink = menuItemRepository.save(drink);
		drink.addFoodCategories(foodCats);
		drinks.add(drink);
	}

	private void createCustomer(String firstName, String lastName, String email, String postalCode, String houseNumber,
			String phoneNumber, DateTime birthDate, int partySize, String description, byte[] photo) {
		Customer customer = new Customer(firstName, lastName, email, postalCode, houseNumber, phoneNumber, birthDate,
				partySize, description, photo);
		customers.add(customer);
		customerRepository.save(customer);
	}

	private void createDiningTables(int numberOfTables, Restaurant restaurant, boolean createOne) {
		int tables = 0;
		if (createOne == true) {
			tables = numberOfTables--;
		}
		for (int i = tables; i < numberOfTables; i++) {
			DiningTable diningTable = new DiningTable(i + 1);
			diningTable.setRestaurant(restaurant);
			restaurant.getDiningTables().add(diningTable);
		}
	}

	// create single dining table with unique id
	private DiningTable createDiningTableWithId(int id, Restaurant restaurant) {
		DiningTable diningTable = new DiningTable(id);
		diningTable.setRestaurant(restaurant);
		restaurant.getDiningTables().add(diningTable);
		
		return diningTable;
	}

	private Restaurant populateRestaurant(Restaurant restaurant) {

		// will save everything that is reachable by cascading
		// even if it is linked to the restaurant after the save
		// operation
		restaurant = restaurantRepository.save(restaurant);

		// every restaurant has its own dining tables
		createDiningTables(5, restaurant, false);

		// for the moment every restaurant has all available food categories
		for (FoodCategory foodCat : foodCats) {
			restaurant.getMenu().getFoodCategories().add(foodCat);
		}

		// for the moment every restaurant has the same menu
		for (Meal meal : meals) {
			restaurant.getMenu().getMeals().add(meal);
		}

		// for the moment every restaurant has the same menu
		for (Drink drink : drinks) {
			restaurant.getMenu().getDrinks().add(drink);
		}

		// for the moment every restaurant has the same meal options
		for (MealOption mealOption : mealOptions) {
			restaurant.getMenu().getMealOptions().add(mealOption);
		}

		// for the moment, every customer has dined in every restaurant
		// no cascading between customer and restaurant; therefore both
		// restaurant and customer
		// must have been saved before linking them one to another
		for (Customer customer : customers) {
			customer.getRestaurants().add(restaurant);
			restaurant.getCustomers().add(customer);
		}

		return restaurant;

	}

	@Override
	public void createRestaurantsWithInventory() {

		createCommonEntities();

		Restaurant restaurant = new Restaurant(HARTIGEHAP_RESTAURANT_NAME, "deHartigeHap.jpg");
		restaurant = populateRestaurant(restaurant);
		DiningTable table = createDiningTableWithId(9999999, restaurant);

		restaurant = new Restaurant(PITTIGEPANNEKOEK_RESTAURANT_NAME, "dePittigePannekoek.jpg");
		restaurant = populateRestaurant(restaurant);

		restaurant = new Restaurant(HMMMBURGER_RESTAURANT_NAME, "deHmmmBurger.jpg");
		restaurant = populateRestaurant(restaurant);

		/////////////////////// ORDER DECORATOR FREEHAND TEST
		/////////////////////// ////////////////////////////////////////

		// how to ensure in the GUI that only pizza options can be added to
		// pizza's?:
		// * use food category to distinguish pizza options from other options
		// (use queries
		// for showing it in the GUI)
		// * add a new category to distinguish pizza options from other options
		// (use queries
		// for showing it in the GUI)
		// * add a different collection in Menu for each type of option is a bad
		// idea, because
		// it hard-codes specific information

		// easy option to show the options in the GUI:
		// * Show on one page the menu item with its options as a radio box and
		// a quantity
		// for each option.
		// * All that information goes in one form to the controller and from
		// there to the service impl.
		// * The service impl creates the decorators
		
		Restaurant restaurantHartigeHap = restaurantService.fetchWarmedUp("HartigeHap");
		
		// create test customer
		Customer customer = new Customer();
		customer.setFirstName("Perry");
		customer.setLastName("Faro");
		customer.setEmail("p.faro@gmail.com");
		customer.setHouseNumber("7");
		customer.setPhoneNumber("0000000000");
		customer.setPostalCode("1111AA");
		customer.setRestaurants(Arrays.asList(new Restaurant[] { restaurantHartigeHap }));
		customer = customerRepository.save(customer);


		// create bill
		Bill bill = new Bill();
		bill.setCustomer(customer);
		bill.setDiningTable(table);
		bill.setBillStatus(BillStatus.SUBMITTED);
		bill = billService.save(bill);
		
		BaseOrderItem basePizza = billService.addOrderItemOnline(bill.getId(), "Pizza Shoarma");
		BaseOrderItem pizzaShoarmaWithCheese = billService.addOrderOptionOnline(bill.getId(), basePizza, "Extra kaas" );
		BaseOrderItem pizzaShoarmaWithCheeseAndTomato = billService.addOrderOptionOnline(bill.getId(), pizzaShoarmaWithCheese, "Extra tomaat" );
		
		Order order = bill.getCurrentOrder();
		order.setOrderStatus(new OrderSubmittedState());
		order.setOrderType(OrderType.ONLINE);


	}
}
