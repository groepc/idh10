package edu.avans.hartigehap.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.avans.hartigehap.domain.MealOption;
import edu.avans.hartigehap.domain.MenuItem;

public interface MenuItemRepository extends PagingAndSortingRepository<MenuItem, String> {
}