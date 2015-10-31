package edu.avans.hartigehap.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HTTPErrorHandlerController {
    @RequestMapping(value = "/404")
    public String error404() {
        return "404";
    }

    @RequestMapping(value = "/400")
    public String error400() {
        return "400";
    }
}