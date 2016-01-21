package edu.avans.hartigehap.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import edu.avans.hartigehap.domain.Owner;
import edu.avans.hartigehap.domain.Restaurant;

public interface OwnerRepository extends PagingAndSortingRepository<Owner, Long> {
	List<Owner> findByRestaurants(Collection<Restaurant> restaurants, Sort sort);

	List<Owner> findByName(String name);
}