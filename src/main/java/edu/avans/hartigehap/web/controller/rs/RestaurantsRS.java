package edu.avans.hartigehap.web.controller.rs;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import edu.avans.hartigehap.domain.Restaurant;
import edu.avans.hartigehap.service.RestaurantService;

// http://briansjavablog.blogspot.nl/2012/08/rest-services-with-spring.html
@Controller
@Slf4j
public class RestaurantsRS {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private View jsonView;

    private static final String DATA_FIELD = "data";
    private static final String ERROR_FIELD = "error";

    /**
     * list all restaurants.
     * 
     * This method can be tested by: curl -i -X GET -H "Accept:application/json"
     * http://localhost:8080/hh/rest/v1/restaurants
     * 
     * @return
     */
    @RequestMapping(value = RSConstants.URL_PREFIX
            + "/restaurants", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Restaurant> restaurants() {
        log.debug("");
        return restaurantService.findAll();
    }

    /**
     * create a new restaurant.
     * 
     * This method can be tested by: curl -i -X POST -H
     * "Content-Type:application/json" -d @restaurantsimple.json
     * http://localhost:8080/hh/rest/v1/restaurants
     * 
     * where restaurantsimple.json is a file with content:
     * {"id":"Braadworstelaars","version":0,"imageFileName":"deHartigeHap.jpg"}
     * 
     * In the GUI, the new restaurant Braadworstelaars will appear.
     */
    @RequestMapping(value = RSConstants.URL_PREFIX
            + "/restaurants", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ModelAndView createRestaurantJson(@RequestBody Restaurant restaurant, HttpServletResponse httpResponse,
            WebRequest httpRequest) {
        log.debug("body: {}", restaurant);

        try {
            Restaurant savedRestaurant = restaurantService.save(restaurant);
            httpResponse.setStatus(HttpStatus.CREATED.value());
            httpResponse.setHeader("Location",
                    httpRequest.getContextPath() + "/restaurants/" + savedRestaurant.getId());
            return new ModelAndView(jsonView, DATA_FIELD, savedRestaurant);
        } catch (Exception e) {
            log.error("Error creating new restaurant", e);
            String message = "Error creating new restaurant. [%1$s]";
            return createErrorResponse(String.format(message, e.toString()));
        }
    }

    @RequestMapping(value = RSConstants.URL_PREFIX
            + "/restaurants/{restaurantId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Restaurant findById(@PathVariable String restaurantId) {
        log.debug("restaurantId: {}", restaurantId);
        return restaurantService.findById(restaurantId);
    }

    private ModelAndView createErrorResponse(String sMessage) {
        return new ModelAndView(jsonView, ERROR_FIELD, sMessage);
    }

    public void setRestaurantService(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    public void setJsonView(View jsonView) {
        this.jsonView = jsonView;
    }

}
