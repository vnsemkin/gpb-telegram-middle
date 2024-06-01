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
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerReqDto;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.CustomerRespDto;
import org.vnsemkin.semkinmiddleservice.application.mappers.CustomerMapper;
import org.vnsemkin.semkinmiddleservice.domain.models.Customer;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;
import org.vnsemkin.semkinmiddleservice.domain.services.CustomerRegistrationService;
import org.vnsemkin.semkinmiddleservice.presentation.controllers.AppController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AppController.class)
public class AppControllerTest {
    private final static String REG_FAULT = "Registration failed";
    private final static String REG_URL = "/registration";
    private final static String NAME = "John";
    private final static long LOCAL_ID = 123456789L;
    private final static String EMAIL = "john@example.com";
    private final static String PASSWORD = "password";
    private final static String UUID = "123e4567-e89b-12d3-a456-426614174000";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRegistrationService customerRegistrationService;

    @MockBean
    private CustomerMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenRegistrationIsSuccessful_thenReturns201() throws Exception {
        CustomerReqDto customerReqDto = new CustomerReqDto(NAME, EMAIL, PASSWORD);
        Customer customer = new Customer(LOCAL_ID, NAME, EMAIL, PASSWORD, UUID);
        CustomerRespDto customerRespDto = new CustomerRespDto(NAME, EMAIL);

        when(customerRegistrationService.register(any(CustomerReqDto.class)))
            .thenReturn(Result.success(customer));
        when(mapper.toDto(any(Customer.class)))
            .thenReturn(customerRespDto);

        mockMvc.perform(post(REG_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerReqDto)))
            .andExpect(status().isCreated());
    }

    @Test
    void whenRegistrationFails_thenReturns400() throws Exception {
        CustomerReqDto customerReqDto = new CustomerReqDto(NAME, EMAIL, PASSWORD);

        when(customerRegistrationService.register(any(CustomerReqDto.class)))
            .thenReturn(Result.failure(new RuntimeException(REG_FAULT)));

        mockMvc.perform(post(REG_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerReqDto)))
            .andExpect(status().isBadRequest());
    }
}
