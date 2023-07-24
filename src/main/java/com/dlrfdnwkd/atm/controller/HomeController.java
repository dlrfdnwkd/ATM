package com.dlrfdnwkd.atm.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @RequestMapping(value = "/test")
    public String home() {
        return "come on";
    }
}
