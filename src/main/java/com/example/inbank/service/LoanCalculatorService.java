package com.example.inbank.service;

import com.example.inbank.dto.LoanDataDto;
import com.example.inbank.dto.UserDto;
import com.example.inbank.exception.DebtException;
import com.example.inbank.exception.WrongIdException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoanCalculatorService {
    private Map<String, String> creditModifier;
    private final BigDecimal maxSum = BigDecimal.valueOf(10000);
    private final BigDecimal minSum = BigDecimal.valueOf(2000);
    private final int maxLoanPeriod = 60;

    public LoanCalculatorService() {
        this.creditModifier = new HashMap<>(Map.of(
                "49002010965", "debt",
                "49002010976", "100",
                "49002010987", "300",
                "49002010998", "1000"));
    }


    public LoanDataDto calculateCreditScore(UserDto userDto) throws DebtException, WrongIdException {
            BigDecimal creditScore;
            String personalId = userDto.getPersonalCode();
            BigDecimal loanAmount = BigDecimal.valueOf(userDto.getLoanAmount());
            BigDecimal loanPeriod = BigDecimal.valueOf(userDto.getLoanPeriod());
            if (!creditModifier.containsKey(userDto.getPersonalCode())) {
                throw new WrongIdException("Wrong id!");
            } else if (creditModifier.get(personalId).equals("debt")) {
                throw new DebtException("Debt!");
            }
            creditScore = BigDecimal.valueOf(Long.parseLong(creditModifier.get(personalId)))
                    .divide(loanAmount, MathContext.DECIMAL32).multiply(loanPeriod);
            BigDecimal newLoanAmount = loanAmount.multiply(creditScore).setScale(0, RoundingMode.HALF_UP);
            // if loan (amount * credit score) is between minimum loan amount and maximum loan amount that bank can give
            if (newLoanAmount.compareTo(maxSum) <= 0 && newLoanAmount.compareTo(minSum) >= 0) {
                if (newLoanAmount.compareTo(loanAmount) >= 0) {
                    return LoanDataDto.builder().loanPeriod(loanPeriod).loanAmount(newLoanAmount).decision("Positive").build();
                }
                return LoanDataDto.builder().loanPeriod(loanPeriod).loanAmount(newLoanAmount).decision("Negative").build();
            }
            // if loan (amount * credit score) is bigger than maximum loan amount that bank can give
            else if (newLoanAmount.compareTo(maxSum) > 0) {
                return LoanDataDto.builder().loanPeriod(loanPeriod).loanAmount(maxSum).decision("Positive").build();
            }
            // if loan (amount * credit score) is smaller than minimum loan amount that bank can give
            else if (newLoanAmount.compareTo(minSum) < 0) {
                return calculateNewMonths(userDto);
            }
            return null;
    }

    public LoanDataDto calculateNewMonths(UserDto userDto) {
        Integer months = userDto.getLoanPeriod();
        String personalId = userDto.getPersonalCode();
        BigDecimal loanAmount = BigDecimal.valueOf(userDto.getLoanAmount());
        for (int i = months; i <= maxLoanPeriod; i++) {
            BigDecimal creditScore = BigDecimal.valueOf(Long.parseLong(creditModifier.get(personalId)))
                    .divide(loanAmount, MathContext.DECIMAL32).multiply(BigDecimal.valueOf(i));
            BigDecimal newLoanAmount = loanAmount.multiply(creditScore).setScale(0, RoundingMode.HALF_UP);
            if (newLoanAmount.compareTo(minSum) >= 0) {
                return LoanDataDto.builder().loanAmount(newLoanAmount).loanPeriod(BigDecimal.valueOf(i)).decision("Negative").build();
            }
        }
        return LoanDataDto.builder().loanPeriod(null).loanPeriod(null).decision("Negative").build();
    }
}
