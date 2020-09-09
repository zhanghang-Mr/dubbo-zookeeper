package com.zh.controller;


import com.zh.service.ProviderService;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/provider")
public class TestProviderController {

    @Autowired
    ProviderService providerService;

    @GetMapping
    public String testProdiver(){
        return providerService.testConsummer();
    }

}
