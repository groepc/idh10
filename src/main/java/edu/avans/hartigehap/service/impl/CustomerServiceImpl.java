package edu.avans.hartigehap.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import edu.avans.hartigehap.domain.Customer;
import edu.avans.hartigehap.domain.Restaurant;
import edu.avans.hartigehap.repository.CustomerRepository;
import edu.avans.hartigehap.service.CustomerService;
import lombok.extern.slf4j.Slf4j;

@Service("customerService")
@Repository
@Transactional
@Slf4j
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Customer> findAll() {
		return Lists.newArrayList(customerRepository.findAll());
	}

	@Override
	@Transactional(readOnly = true)
	public Customer findById(Long id) {
		return customerRepository.findOne(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Customer findByFirstNameAndLastName(String firstName, String lastName) {

		Customer customer = null;

		List<Customer> customers = customerRepository.findByFirstNameAndLastName(firstName, lastName);
		if (!customers.isEmpty()) {
			customer = customers.get(0);
		}
		return customer;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Customer> findCustomersForRestaurant(Restaurant restaurant) {

		// a query created using a repository method name
		List<Customer> customersForRestaurants = customerRepository.findByRestaurants(
				Arrays.asList(new Restaurant[] { restaurant }), new Sort(Sort.Direction.ASC, "lastName"));

		log.info("findCustomersForRestaurant using query created using repository method name");
		ListIterator<Customer> it = customersForRestaurants.listIterator();
		while (it.hasNext()) {
			Customer customer = it.next();
			log.info("customer = " + customer);
		}

		return customersForRestaurants;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Customer> findAllByPage(Pageable pageable) {
		return customerRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Customer> findCustomersForRestaurantByPage(Restaurant restaurant, Pageable pageable) {
		// a query created using a repository method name
		Page<Customer> customersForRestaurants = customerRepository
				.findByRestaurants(Arrays.asList(new Restaurant[] { restaurant }), pageable);

		log.info("findCustomersForRestaurant using query created using repository method name");
		Iterator<Customer> it = customersForRestaurants.iterator();
		while (it.hasNext()) {
			Customer customer = it.next();
			log.info("customer = " + customer);
		}

		return customersForRestaurants;
	}

	@Override
	public Customer save(Customer customer) {
		return customerRepository.save(customer);
	}

	@Override
	public void delete(Long id) {
		customerRepository.delete(id);
	}

}
