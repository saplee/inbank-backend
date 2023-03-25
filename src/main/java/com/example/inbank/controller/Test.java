package com.example.inbank.controller;


import com.example.inbank.dto.LoanDataDto;
import com.example.inbank.dto.UserDto;
import com.example.inbank.service.LoanCalculatorService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
public class Test {
    private final LoanCalculatorService loanCalculatorService;

    public Test(LoanCalculatorService loanCalculatorService) {
        this.loanCalculatorService = loanCalculatorService;
    }

    @GetMapping("test")
    public String testEndPoint() {
        return "INBANK";
    }

    @PostMapping("user/loan")
    public LoanDataDto tear(@RequestBody UserDto userDto){
        return loanCalculatorService.calculateCreditScore(userDto);
    }

}
