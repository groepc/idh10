package edu.avans.hartigehap.web.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.avans.hartigehap.domain.Restaurant;
import edu.avans.hartigehap.service.RestaurantService;

@Controller
@PreAuthorize("hasRole('ROLE_DELIVERER')")
public class DelivererController {

	@Autowired
    private RestaurantService restaurantService;
	
	@RequestMapping(value = "/restaurants/{restaurantName}/deliverer", method = RequestMethod.GET)
    public String showDeliverer(@PathVariable("restaurantName") String restaurantName, Model uiModel) {

        // warmup stuff
        Collection<Restaurant> restaurants = restaurantService.findAll();
        uiModel.addAttribute("restaurants", restaurants);
        Restaurant restaurant = restaurantService.fetchWarmedUp(restaurantName);
        uiModel.addAttribute("restaurant", restaurant);

        return "hartigehap/deliverer";
    }
	
}
