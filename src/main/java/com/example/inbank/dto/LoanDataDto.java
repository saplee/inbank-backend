package com.example.inbank.dto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LoanDataDto {
    private BigDecimal loanAmount;
    private BigDecimal loanPeriod;
    private String decision;
}
