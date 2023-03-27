package com.example.inbank.controller;

import org.springframework.web.bind.annotation.*;


@RestController
public class Test {

    @GetMapping("test")
    public String testEndPoint() {
        return "INBANK";
    }
}
