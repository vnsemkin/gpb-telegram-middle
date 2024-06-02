package org.vnsemkin.semkinmiddleservice.services;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.vnsemkin.semkinmiddleservice.SemkinMiddleServiceApplicationTests;
import org.vnsemkin.semkinmiddleservice.domain.services.PasswordService;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordServiceTest extends SemkinMiddleServiceApplicationTests {
    final static String PASSWORD = "password";
    final static String HASHED_PASSWORD = "$2a$10$/7rukcWx053I1ESyqShGJugQ28wjc2JukJTpEMR4tQo9AW2T.DI4W";

    @InjectMocks
    PasswordService passwordService;

    @Test
    public void testHashPassword() {
        String result = passwordService.hashPassword(PASSWORD);
        assertNotNull(result);
        assertNotEquals(PASSWORD, result);
    }

    @Test
    public void testCheckPassword() {
        boolean result = passwordService.checkPassword(PASSWORD, HASHED_PASSWORD);
        assertTrue(result);
    }
}