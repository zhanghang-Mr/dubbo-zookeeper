package com.zh.controller;


import com.zh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consummer")
public class TestConsummerController {

    @Autowired
    UserService userService;

    @GetMapping
    public String testConsummer() {
        return userService.testProvider();
    }
}
