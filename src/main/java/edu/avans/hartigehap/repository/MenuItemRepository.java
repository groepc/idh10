package edu.avans.hartigehap.repository;

import java.util.Collection;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.avans.hartigehap.domain.MenuItem;

public interface MenuItemRepository extends PagingAndSortingRepository<MenuItem, String> {
	Collection<MenuItem> findByRecipe(String name);
}