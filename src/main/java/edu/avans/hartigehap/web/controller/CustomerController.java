package edu.avans.hartigehap.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.avans.hartigehap.domain.Customer;
import edu.avans.hartigehap.domain.Restaurant;
import edu.avans.hartigehap.service.CustomerService;
import edu.avans.hartigehap.service.RestaurantService;
import edu.avans.hartigehap.web.form.Message;
import edu.avans.hartigehap.web.util.UrlUtil;
import lombok.extern.slf4j.Slf4j;

@Controller
@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
@Slf4j
public class CustomerController {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private RestaurantService restaurantService;

    @RequestMapping(value = "/restaurants/{restaurantName}/customers", method = RequestMethod.GET)
    public String listCustomers(@PathVariable("restaurantName") String restaurantName, Model uiModel) {
        Restaurant restaurant = warmupRestaurant(restaurantName, uiModel);

        log.info("Listing customers");
        List<Customer> customers = customerService.findCustomersForRestaurant(restaurant);
        uiModel.addAttribute("customers", customers);
        log.info("No. of customers: " + customers.size());

        return "hartigehap/listcustomers";
    }

    @RequestMapping(value = "/restaurants/{restaurantName}/customers/{id}", method = RequestMethod.GET)
    public String showCustomer(@PathVariable("restaurantName") String restaurantName, @PathVariable("id") Long id,
            Model uiModel) {

        warmupRestaurant(restaurantName, uiModel);

        log.info("Show customer: " + id);

        Customer customer = customerService.findById(id);
        uiModel.addAttribute("customer", customer);
        return "hartigehap/showcustomer";
    }

    @RequestMapping(value = "/restaurants/{restaurantName}/customers/{id}", params = "form", method = RequestMethod.GET)
    public String updateCustomerForm(@PathVariable("restaurantName") String restaurantName, @PathVariable("id") Long id,
            Model uiModel) {

        warmupRestaurant(restaurantName, uiModel);

        log.info("Customer update form for customer: " + id);

        Customer customer = customerService.findById(id);
        uiModel.addAttribute("customer", customer);
        log.info("updatingCustomerForm(" + customer.getFirstName() + ", " + customer.getLastName() + ")");
        return "hartigehap/editcustomer";
    }
    
    @RequestMapping(value = "/restaurants/{restaurantName}/customers", params = "form", method = RequestMethod.GET)
    public String createCustomerForm(@PathVariable("restaurantName") String restaurantName, Model uiModel) {

        warmupRestaurant(restaurantName, uiModel);

        log.info("Create customer form");

        Customer customer = new Customer();
        uiModel.addAttribute("customer", customer);
        return "hartigehap/editcustomer";
    }

    private String handleCreateOrUpdateCustomer(boolean isCreate, String restaurantName, Customer customer, BindingResult bindingResult, Model uiModel,
            HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes, Locale locale, Part file) {
        
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("message",
                    new Message("error", messageSource.getMessage("customer_save_fail", new Object[] {}, locale)));
            uiModel.addAttribute("customer", customer);
            return "hartigehap/editcustomer";
        }
        uiModel.asMap().clear();
        redirectAttributes.addFlashAttribute("message",
                new Message("success", messageSource.getMessage("customer_save_success", new Object[] {}, locale)));

        processUploadedFile(customer, file);

        if(isCreate) {
            // relate customer to current restaurant
            Restaurant restaurant = warmupRestaurant(restaurantName, uiModel);
            customer.setRestaurants(Arrays.asList(new Restaurant[] { restaurant }));
            // to get the auto generated id
            customer = customerService.save(customer);
        } else { // update
            Customer existingCustomer = customerService.findById(customer.getId());
            assert existingCustomer != null : "customer should exist";

            // update user-editable fields
            existingCustomer.updateEditableFields(customer);
            customerService.save(existingCustomer);
        }
        
        return "redirect:/restaurants/" + restaurantName + "/customers/"
                + UrlUtil.encodeUrlPathSegment(customer.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/restaurants/{restaurantName}/customers/{id}", params = "form", method = RequestMethod.PUT)
    public String updateCustomer(
            // the path variable {id} is not used; data binding retrieves its info from
            // query string parameters and form fields, so customer includes id as well
            @PathVariable("restaurantName") String restaurantName, @Valid Customer customer,
            BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest,
            RedirectAttributes redirectAttributes, Locale locale, @RequestParam(required = false) Part file) {

        return handleCreateOrUpdateCustomer(false, restaurantName, customer, bindingResult, uiModel,
                httpServletRequest, redirectAttributes, locale, file);
        }

    @RequestMapping(value = "/restaurants/{restaurantName}/customers", params = "form", method = RequestMethod.POST)
    public String createCustomer(@PathVariable("restaurantName") String restaurantName, @Valid Customer customer,
            BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest,
            RedirectAttributes redirectAttributes, Locale locale,
            @RequestParam(value = "file", required = false) Part file) {

        log.info("Creating customer: " + customer.getFirstName() + " " + customer.getLastName());
        log.info("Binding Result target: " + (Customer) bindingResult.getTarget());
        log.info("Binding Result: " + bindingResult);

        return handleCreateOrUpdateCustomer(true, restaurantName, customer, bindingResult, uiModel,
                httpServletRequest, redirectAttributes, locale, file);
    }

    private void processUploadedFile(Customer customer, Part file) {
        if (file != null) {
            log.info("File name: " + file.getName());
            log.info("File size: " + file.getSize());
            log.info("File content type: " + file.getContentType());
            byte[] fileContent = null;
            try {
                InputStream inputStream = file.getInputStream();
                if (inputStream == null) {
                    log.info("File inputstream is null");
                }
                fileContent = IOUtils.toByteArray(inputStream);
                customer.setPhoto(fileContent);
                log.info("****erco " + customer.getPhoto().length);
            } catch (IOException ex) {
                log.error("Error saving uploaded file", ex);
            }
            customer.setPhoto(fileContent);
        }
    }
    
    
    @RequestMapping(value = "/restaurants/{restaurantName}/customers/{id}/photo", method = RequestMethod.GET)
    @ResponseBody
    public byte[] downloadPhoto(@PathVariable("id") Long id) {
        Customer customer = customerService.findById(id);
        if (customer.getPhoto() != null) {
            log.info("Downloading photo for id: {} with size: {}", customer.getId(), customer.getPhoto().length);
        }
        return customer.getPhoto();
    }

    // to be truly RESTful use DELETE instead of GET
    @RequestMapping(value = "/restaurants/{restaurantName}/customers/{id}", params = "delete", method = RequestMethod.GET)
    public String delete(@PathVariable("restaurantName") String restaurantName, @PathVariable("id") Long id) {

        log.info("Deleting customer: " + id);
        customerService.delete(id);
        return "redirect:/restaurants/" + restaurantName + "/customers/";
    }

    private Restaurant warmupRestaurant(String restaurantName, Model uiModel) {
        Collection<Restaurant> restaurants = restaurantService.findAll();
        uiModel.addAttribute("restaurants", restaurants);
        Restaurant restaurant = restaurantService.fetchWarmedUp(restaurantName);
        uiModel.addAttribute("restaurant", restaurant);
        return restaurant;
    }

}