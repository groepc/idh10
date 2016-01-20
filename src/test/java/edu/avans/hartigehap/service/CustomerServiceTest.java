package edu.avans.hartigehap.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import edu.avans.hartigehap.domain.Customer;
import edu.avans.hartigehap.service.testutil.AbstractTransactionRollbackTest;

public class CustomerServiceTest extends AbstractTransactionRollbackTest {

	private static final String CUSTOMER_FIRST_NAME = "Harm";
	private static final String CUSTOMER_LAST_NAME = "Happerdepap";
	private static final String CUSTOMER_POSTALCODE = "5049LJ";
	private static final String CUSTOMER_HOUSENUMBER = "2";
	private static final String CUSTOMER_EMAIL = "test@hartigehap.nl";
	private static final String CUSTOMER_FIRST_NAME2 = "Eibert";
	private static final String CUSTOMER_LAST_NAME2 = "Draisma";
	private static final String CUSTOMER_POSTALCODE2 = "3235SE";
	private static final String CUSTOMER_HOUSENUMBER2 = "3";
	private static final String CUSTOMER_EMAIL2 = "test2@hartigehap.nl";

	@Autowired
	private CustomerService customerService;

	@Test
	public void dummy() {
		// empty - tests configuration of test context.
	}

	@Test
	public void create() {
		// execute
		Customer customer = createCustomer(CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME, CUSTOMER_POSTALCODE,
				CUSTOMER_HOUSENUMBER, CUSTOMER_EMAIL);

		// verify
		List<Customer> customers = customerService.findAll();
		assertNotNull(customers);
		assertTrue("created customer in list", customers.contains(customer));
	}

	@Test
	public void findByName() {
		// prepare
		Customer customer = createCustomer(CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME, CUSTOMER_POSTALCODE,
				CUSTOMER_HOUSENUMBER, CUSTOMER_EMAIL);

		// execute
		Customer customer2 = customerService.findByFirstNameAndLastName(CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME);

		// verify
		assertTrue("created customer in findByFirstNameAndLastName", customer.equals(customer2));
	}

	@Test
	public void delete() {
		// prepare
		Customer customer = createCustomer(CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME, CUSTOMER_POSTALCODE,
				CUSTOMER_HOUSENUMBER, CUSTOMER_EMAIL);
		List<Customer> customers = customerService.findAll();
		assertNotNull(customers);
		assertTrue("created customer in list", customers.contains(customer));

		// execute
		customerService.delete(customer.getId());

		// verify
		List<Customer> customers2 = customerService.findAll();
		assertNotNull(customers2);
		assertFalse("deleted customer not in the list", customers2.contains(customer));
	}

	@Test
	public void update() {
		Customer customer = createCustomer(CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME, CUSTOMER_POSTALCODE,
				CUSTOMER_HOUSENUMBER, CUSTOMER_EMAIL);
		customer.setFirstName(CUSTOMER_FIRST_NAME2);
		customer.setLastName(CUSTOMER_LAST_NAME2);
		customer.setPostalCode(CUSTOMER_POSTALCODE2);
		customer.setHouseNumber(CUSTOMER_HOUSENUMBER2);
		customer.setEmail(CUSTOMER_EMAIL2);

		Customer customer2 = customerService.findById(customer.getId());
		assertEquals("firstName", CUSTOMER_FIRST_NAME2, customer2.getFirstName());
		assertEquals("lastName", CUSTOMER_LAST_NAME2, customer2.getLastName());
		assertEquals("postalCode", CUSTOMER_POSTALCODE2, customer2.getPostalCode());
		assertEquals("houseNumber", CUSTOMER_HOUSENUMBER2, customer2.getHouseNumber());
		assertEquals("email", CUSTOMER_EMAIL2, customer2.getEmail());
	}

	private Customer createCustomer(String firstName, String lastName, String postalCode, String houseNumber,
			String email) {
		Customer customer = new Customer();
		customer.setFirstName(firstName);
		customer.setLastName(lastName);
		customer.setPostalCode(postalCode);
		customer.setHouseNumber(houseNumber);
		customer.setEmail(email);
		Customer retval = customerService.save(customer);
		assertNotNull(retval);
		assertNotNull(retval.getId());
		assertEquals("firstName", firstName, retval.getFirstName());
		assertEquals("lastName", lastName, retval.getLastName());
		assertEquals("postalCode", postalCode, retval.getPostalCode());
		assertEquals("houseNumber", houseNumber, retval.getHouseNumber());
		assertEquals("email", email, retval.getEmail());
		return retval;
	}

}
