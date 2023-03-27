package com.example.inbank.controller;
import com.example.inbank.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLoanWithNewLoanPeriod() throws Exception {
        UserDto userDto = UserDto.builder().personalCode("49002010976").loanPeriod(12).loanAmount(2000).build();
        mockMvc.perform(post("/user/loan").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanPeriod").value(20));
    }
    @Test
    void testLoanMaxAmount() throws Exception {
        UserDto userDto = UserDto.builder().personalCode("49002010998").loanPeriod(60).loanAmount(9000).build();
        mockMvc.perform(post("/user/loan").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanAmount").value(10000));
    }
    @Test
    void testWrongId() throws Exception {
        UserDto userDto = UserDto.builder().personalCode("57654574").loanPeriod(60).loanAmount(9000).build();
        mockMvc.perform(post("/user/loan").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNotFound());
    }


    @Test
    void testPersonInDebt() throws Exception {
        UserDto userDto = UserDto.builder().personalCode("49002010965").loanPeriod(60).loanAmount(9000).build();
        mockMvc.perform(post("/user/loan").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isConflict());
    }
}