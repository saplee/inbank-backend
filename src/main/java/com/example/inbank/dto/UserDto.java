package com.example.inbank.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String personalCode;
    private Integer loanAmount;
    private Integer loanPeriod;
}