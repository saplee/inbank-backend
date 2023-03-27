package com.example.inbank.controller;

import com.example.inbank.dto.LoanDataDto;
import com.example.inbank.dto.UserDto;
import com.example.inbank.exception.DebtException;
import com.example.inbank.exception.WrongIdException;
import com.example.inbank.service.LoanCalculatorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoanController {
    private final LoanCalculatorService loanCalculatorService;

    public LoanController(LoanCalculatorService loanCalculatorService) {
        this.loanCalculatorService = loanCalculatorService;
    }

    @PostMapping("user/loan")
    public LoanDataDto getLoan(@RequestBody UserDto userDto) throws DebtException, WrongIdException {
        return loanCalculatorService.calculateCreditScore(userDto);
    }
}
