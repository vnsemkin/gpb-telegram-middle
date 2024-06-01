package org.vnsemkin.semkinmiddleservice.web_client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.vnsemkin.semkinmiddleservice.SemkinMiddleServiceApplicationTests;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.ResultBackendDto;
import org.vnsemkin.semkinmiddleservice.models.ErrorModel;
import org.vnsemkin.semkinmiddleservice.presentation.web_client.BackendClientInterfaceImp;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class BackendWebClientTest extends SemkinMiddleServiceApplicationTests {
    private final static String USER_CREATED = "User created.";
    private final static String TEST = "Test";
    private final static long LOCAL_ID = 1L;
    @Autowired
    BackendClientInterfaceImp backendWebClient;
    @Autowired
    ObjectMapper objectMapper;

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setup() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        System.setProperty("backend.base-url",
            "http://localhost:" + wireMockServer.port());
    }

    @AfterAll
    public static void teardown() {
        wireMockServer.stop();
    }

    @Test
    public void whenRegisterCustomerOnBackend_Success() {
        // ARRANGE
        stubFor(post(urlEqualTo("/users"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.NO_CONTENT.value())));
        // ACT
        ResultBackendDto<?> resultBackendDto = backendWebClient
            .registerCustomerOnBackend(LOCAL_ID);
        //ASSERT
        verify(postRequestedFor(urlEqualTo("/users")));
        assertNotNull(resultBackendDto);
        assertTrue(resultBackendDto.isSuccess());
        assertTrue(resultBackendDto.getData().isPresent());
        assertEquals(resultBackendDto.getData().get(), USER_CREATED);
    }

    @Test
    public void whenRegisterCustomerOnBackend_Error() throws JsonProcessingException {
        // ARRANGE
        ErrorModel errorModel = new ErrorModel(TEST, TEST, TEST, TEST);
        String modelErrorAsJson = objectMapper.writeValueAsString(errorModel);
        stubFor(post(urlEqualTo("/users"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(modelErrorAsJson)));
        // ACT
        ResultBackendDto<?> resultBackendDto = backendWebClient.registerCustomerOnBackend(LOCAL_ID);
        //ASSERT
        verify(postRequestedFor(urlEqualTo("/users")));
        assertNotNull(resultBackendDto);
        assertTrue(resultBackendDto.isFailure());
        assertFalse(resultBackendDto.isSuccess());
        assertTrue(resultBackendDto.getError().isPresent());
        assertEquals(resultBackendDto.getError().get().message(), TEST);
        assertEquals(resultBackendDto.getError().get().code(), TEST);
        assertEquals(resultBackendDto.getError().get().type(), TEST);
        assertEquals(resultBackendDto.getError().get().traceId(), TEST);
    }
}
