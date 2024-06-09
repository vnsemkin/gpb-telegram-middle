package org.vnsemkin.semkinmiddleservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerRegistrationRequest;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerRegistrationResponse;
import org.vnsemkin.semkinmiddleservice.application.mappers.AppMapper;
import org.vnsemkin.semkinmiddleservice.application.usecases.CustomerRegistrationService;
import org.vnsemkin.semkinmiddleservice.domain.models.Account;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;
import org.vnsemkin.semkinmiddleservice.domain.services.AccountService;
import org.vnsemkin.semkinmiddleservice.presentation.controllers.AppController;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AppController.class)
public class AppControllerTest {
    private final static String REG_FAULT = "Registration failed";
    private final static String REG_URL = "/customers";
    private final static long ACCOUNT_ID = 1232232L;
    private final static String ACCOUNT_NAME = "Test";
    private final static BigDecimal BALANCE = new BigDecimal("123.456");
    private final static long TG_USER_ID = 137264783L;
    private final static String TG_USERNAME = "Test";
    private final static String FIRST_NAME = "John";
    private final static long LOCAL_ID = 123456789L;
    private final static String EMAIL = "john@example.com";
    private final static String PASSWORD = "password";
    private final static String UUID = "123e4567-e89b-12d3-a456-426614174000";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRegistrationService customerRegistrationService;

    @MockBean
    private AppMapper mapper;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenCustomerRegistrationIsSuccessful_thenReturns201() throws Exception {
        CustomerRegistrationRequest customerRegistrationRequest =
            new CustomerRegistrationRequest(TG_USER_ID, FIRST_NAME, TG_USERNAME, EMAIL, PASSWORD);
        Account account = new Account(ACCOUNT_ID, UUID, ACCOUNT_NAME, BALANCE);
        Customer customer =
            new Customer(LOCAL_ID, TG_USER_ID, FIRST_NAME, TG_USERNAME, EMAIL, PASSWORD, UUID, account);
        CustomerRegistrationResponse customerRegistrationResponse =
            new CustomerRegistrationResponse(FIRST_NAME, EMAIL);

        when(customerRegistrationService.register(any(CustomerRegistrationRequest.class)))
            .thenReturn(Result.success(customer));
        when(mapper.toCustomerRegistrationResponse(any(Customer.class)))
            .thenReturn(customerRegistrationResponse);

        mockMvc.perform(post(REG_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerRegistrationRequest)))
            .andExpect(status().isCreated());
    }

    @Test
    void whenCustomerRegistrationFails_thenReturns400() throws Exception {
        CustomerRegistrationRequest customerRegistrationRequest =
            new CustomerRegistrationRequest(TG_USER_ID, FIRST_NAME, TG_USERNAME, EMAIL, PASSWORD);

        when(customerRegistrationService.register(any(CustomerRegistrationRequest.class)))
            .thenReturn(Result.error(REG_FAULT));

        mockMvc.perform(post(REG_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerRegistrationRequest)))
            .andExpect(status().isBadRequest());
    }
}