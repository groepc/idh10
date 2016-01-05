package edu.avans.hartigehap.web.controller;

import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.validation.*;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import edu.avans.hartigehap.web.form.*;
import javax.servlet.http.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.avans.hartigehap.web.util.*;
import lombok.extern.slf4j.Slf4j;
import java.util.*;
import edu.avans.hartigehap.domain.*;
import edu.avans.hartigehap.service.*;
import javax.servlet.http.*;

@Controller
@Slf4j
public class OnlineOrderController {
	
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