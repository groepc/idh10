package edu.avans.hartigehap.web.controller;

import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;
import org.springframework.validation.*;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import edu.avans.hartigehap.web.form.*;
import javax.servlet.http.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import edu.avans.hartigehap.web.util.*;
import lombok.extern.slf4j.Slf4j;
import java.util.*;
import edu.avans.hartigehap.domain.*;
import edu.avans.hartigehap.service.*;
import javax.servlet.http.*;

@Controller
@Slf4j
public class OnlineOrderController {
	
    @RequestMapping(value = { "/online-order", "/online-order/step1" }, method = RequestMethod.GET)
    public String onlineOrder() {
    	
    	log.info("Online order step 1");
    	
        return "hartigehap/onlineorder/step1";
    }
    
}