package edu.avans.hartigehap.service;

import java.util.Collection;
import java.util.List;

import edu.avans.hartigehap.domain.BaseFood;
import edu.avans.hartigehap.domain.MenuItem;

public interface BaseFoodService {
	List<BaseFood> findAll();
	
	Collection<MenuItem> findOnlineMenuItems();
}
