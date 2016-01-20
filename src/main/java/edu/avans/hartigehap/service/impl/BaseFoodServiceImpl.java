package edu.avans.hartigehap.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import edu.avans.hartigehap.domain.BaseFood;
import edu.avans.hartigehap.domain.MenuItem;
import edu.avans.hartigehap.repository.BaseFoodRepository;
import edu.avans.hartigehap.repository.FoodCategoryRepository;
import edu.avans.hartigehap.repository.MenuItemRepository;
import edu.avans.hartigehap.service.BaseFoodService;

@Service("foodService")
@Repository
@Transactional
public class BaseFoodServiceImpl implements BaseFoodService {

	@Autowired
	private BaseFoodRepository foodRepository;
	@Autowired
	private MenuItemRepository menuItemRepository;
	@Autowired
	private FoodCategoryRepository foodCategoryRepository;

	@Override
	@Transactional(readOnly = true)
	public List<BaseFood> findAll() {
		// MySQL and H2 return the restaurants of findAll() in different order
		// sorting the result makes the behavior less database vendor dependent
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		return Lists.newArrayList(foodRepository.findAll(sort));
	}

	@Override
	public Collection<MenuItem> findOnlineMenuItems() {
		return menuItemRepository.findByRecipe("online");
	}

}
