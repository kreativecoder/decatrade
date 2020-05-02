package com.decagon.decatrade.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIT {
    private static final String BASE_URI = "http://localhost";

    @Value("${local.server.port}")
    private int port;

    @BeforeAll
    void setup() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }
}
