package edu.avans.hartigehap.web.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import java.util.*;
import edu.avans.hartigehap.domain.*;
import edu.avans.hartigehap.service.*;
import javax.servlet.http.*;

@Controller
@Slf4j
public class OnlineOrderController {
	
	@Autowired
    private BaseFoodService baseFoodService;
	
	/**
	 * STEP 1
	 * @param model
	 * @return
	 */
    @RequestMapping(value = { "/online-order", "/online-order/customer-details" }, method = RequestMethod.GET)
    public String onlineOrderCustomerDetails(Model model) {
    	log.info("Online order step 1, customer details");
        return "hartigehap/onlineorder/customer-details";
    }
    
    /**
	 * STEP 1 process request
	 * @param model
	 * @return
	 */
    @RequestMapping(value = { "/online-order", "/online-order/customer-details" }, method = RequestMethod.POST)
    public String onlineOrderCustomerDetailsProcess(Model model, HttpSession session) {
    	log.info("Online order step 1, customer details Process");
    	
    	
    	 
    	return "redirect:/online-order/select-meals";
    }
    
    /**
	 * STEP 2
	 * @param model
	 * @return
	 */
    @RequestMapping(value = "/online-order/select-meals", method = RequestMethod.GET)
    public String onlineOrderSelectMeals(Model model) {
    	log.info("Online order step 2, select meals");
        Collection<BaseFood> baseFoods = baseFoodService.findAll();
        model.addAttribute("foods", baseFoods);
        return "hartigehap/onlineorder/select-meals";
    }
    
    /**
   	 * STEP 3
   	 * @param model
   	 * @return
   	 */
    @RequestMapping(value = "/online-order/payment", method = RequestMethod.GET)
    public String onlineOrderPayment(Model model) {
    	log.info("Online order step 3, payment");
        return "hartigehap/onlineorder/payment";
    }
    
    /**
   	 * STEP 4
   	 * @param model
   	 * @return
   	 */
    @RequestMapping(value = "/online-order/receipt", method = RequestMethod.GET)
    public String onlineOrderReceipt(Model model) {
    	log.info("Online order step 4, receipt");
        return "hartigehap/onlineorder/receipt";
    }
    
}