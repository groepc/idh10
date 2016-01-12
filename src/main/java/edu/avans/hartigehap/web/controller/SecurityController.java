package edu.avans.hartigehap.web.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.avans.hartigehap.web.form.Message;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class SecurityController {

    @Autowired
    private MessageSource messageSource;

    @RequestMapping("/loginfail")
    public String loginFail(Model uiModel, Locale locale) {
        log.info("Login failed detected");
        uiModel.addAttribute("message",
                new Message("error", messageSource.getMessage("message_login_fail", new Object[] {}, locale)));
        return "hartigehap/listrestaurants";
    }

}
