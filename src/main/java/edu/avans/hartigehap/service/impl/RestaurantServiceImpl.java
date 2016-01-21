package edu.avans.hartigehap.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import edu.avans.hartigehap.aop.MyExecutionTime;
import edu.avans.hartigehap.domain.Restaurant;
import edu.avans.hartigehap.repository.RestaurantRepository;
import edu.avans.hartigehap.service.RestaurantService;

@Service("restaurantService")
@Repository
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Restaurant> findAll() {
		// MySQL and H2 return the restaurants of findAll() in different order
		// sorting the result makes the behavior less database vendor dependent
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		return Lists.newArrayList(restaurantRepository.findAll(sort));
	}

	@Override
	@Transactional(readOnly = true)
	public Restaurant findById(String restaurant) {
		return restaurantRepository.findOne(restaurant);
	}

	@Override
	public Restaurant save(Restaurant restaurant) {
		return restaurantRepository.save(restaurant);
	}

	@Override
	public void delete(String restaurant) {
		restaurantRepository.delete(restaurant);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Restaurant> findAllByPage(Pageable pageable) {
		return restaurantRepository.findAll(pageable);
	}

	/**
	 * to be able to follow associations outside the context of a transaction,
	 * prefetch the associated entities by traversing the associations
	 */
	@Override
	@MyExecutionTime
	@Transactional(readOnly = true)
	public Restaurant fetchWarmedUp(String restaurantName) {
		Restaurant restaurant = restaurantRepository.findOne(restaurantName);
		restaurant.warmup();

		return restaurant;
	}

}
