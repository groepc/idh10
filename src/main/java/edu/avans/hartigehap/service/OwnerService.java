package edu.avans.hartigehap.service;

import java.util.List;

import edu.avans.hartigehap.domain.Owner;

public interface OwnerService {
	List<Owner> findAll();
	Owner findById(Long id);
	List<Owner> findByName(String name);
	Owner save(Owner owner);
	void delete(Long id);
	
	/**
	 * provides the owners of a restaurant.
	 * 
	 * @param restaurantId identifier of the restaurant.
	 * @return list of owners of the restaurant.
	 */
	List<Owner> findByRestaurant(String restaurantId);

	/**
	 * creates an owner for an existing restaurant.
	 * 
	 * @param restaurantId identifier of the restaurant.
	 * @param owner the owner to be created. All properties will be persisted. The
	 * ID property must not be set.
	 * @return newly created owner. The ID property is set.
	 */
	Owner addRestaurantToOwner(String restaurantId, Owner owner);

	/**
	 * removes the association between an Owner and a Restaurant.
	 * 
	 * The entities itself are not deleted.
	 * 
	 * @param ownerId identifier of the Owner.
	 * @param restaurantId identifier of the Restaurant.
	 */
	Owner removeRestaurantFromOwner(Long ownerId, String restaurantId);
}