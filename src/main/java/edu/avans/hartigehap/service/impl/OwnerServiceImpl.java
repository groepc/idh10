package edu.avans.hartigehap.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.collect.Lists;

import edu.avans.hartigehap.domain.Owner;
import edu.avans.hartigehap.domain.Restaurant;
import edu.avans.hartigehap.repository.OwnerRepository;
import edu.avans.hartigehap.service.OwnerService;
import edu.avans.hartigehap.service.RestaurantService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service("ownerService")
@Repository
@Transactional
public class OwnerServiceImpl implements OwnerService {

	private final Logger logger = LoggerFactory.getLogger(OwnerServiceImpl.class);

	@Autowired
	private OwnerRepository ownerRepository;
	@Autowired
	private RestaurantService restaurantService;

	@Override
	@Transactional(readOnly = true)
	public List<Owner> findAll() {
		List<Owner> retval = Lists.newLinkedList(ownerRepository.findAll());
		logger.info("" + retval);
		return retval;
	}

	@Override
	@Transactional(readOnly = true)
	public Owner findById(Long id) {
		return ownerRepository.findOne(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Owner> findByName(String name) {
		return ownerRepository.findByName(name);
	}

	@Override
	public Owner save(Owner owner) {
		return ownerRepository.save(owner);
	}

	@Override
	public void delete(Long id) {
		ownerRepository.delete(id);
	}

	@Override
	public List<Owner> findByRestaurant(String restaurantId) {
		Restaurant restaurant = restaurantService.findById(restaurantId);
		return ownerRepository.findByRestaurants(Arrays.asList(new Restaurant[] { restaurant }),
				new Sort(Sort.Direction.ASC, "name"));
	}

	@Override
	public Owner addRestaurantToOwner(String restaurantId, Owner owner) {
		// start tx - restaurant now attached. The owner is detached.
		Restaurant restaurant = restaurantService.findById(restaurantId);
		logger.debug("restaurant: {}", restaurant);
		if (restaurant == null) {
			logger.debug("Cannot find Restaurant {}", restaurantId);
			return null;
		}

		if (!owner.getRestaurants().contains(restaurant)) {
			owner.getRestaurants().add(restaurant);
			restaurant.getOwners().add(owner);
		}
		Owner ownerSaved = ownerRepository.save(owner);
		logger.debug("return: {}", ownerSaved);
		return ownerSaved;
	}

	@Override
	public Owner removeRestaurantFromOwner(Long ownerId, String restaurantId) {
		Restaurant restaurant = restaurantService.findById(restaurantId);
		if (restaurant == null) {
			throw new RuntimeException("restaurant not found " + restaurantId);
		}
		Owner owner = ownerRepository.findOne(ownerId);
		if (owner == null) {
			logger.debug("owner not found {} - return", ownerId);
			return null;
		}

		Collection<Restaurant> restaurants = owner.getRestaurants();
		if (!restaurants.contains(restaurant)) {
			logger.debug("restaurant {} not associated to customer - return", restaurantId, ownerId);
			logger.debug("return: {}", owner);
			return owner;
		}

		restaurants.remove(restaurant);
		Owner retval = ownerRepository.save(owner);
		logger.debug("return: {}", retval);
		return retval;
	}
}