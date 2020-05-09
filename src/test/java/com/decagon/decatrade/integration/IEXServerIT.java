package com.decagon.decatrade.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasKey;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IEXServerIT {
    private static final String BASE_URI = "https://cloud.iexapis.com/stable/";
    private static final String API_TOKEN = "pk_d024f60c01db434ebcc6a3aa20baeaf3";

    @BeforeAll
    void setup() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    public void testServerStatus() {
        get("/status")
            .then()
            .assertThat()
            .statusCode(SC_OK)
            .body("status", is("up"));
    }

    @Test
    public void testGetStockDetails() {
        get("/stock/nflx/quote/?token=" + API_TOKEN)
            .then()
            .assertThat()
            .statusCode(SC_OK)
            .body("symbol", is("NFLX"),
                "primaryExchange", is("NASDAQ"),
                "$", hasKey("latestPrice"));
    }

    @Test
    public void testGetStockDetailsWrongSymbol() {
        given()
            .contentType(JSON)
            .accept(JSON)
            .get("/stock/abiola/quote/?token=" + API_TOKEN)
            .then()
            .assertThat()
            .statusCode(SC_NOT_FOUND);
    }

    @Test
    public void getAllSymbols() {
        given()
            .contentType(JSON)
            .accept(JSON)
            .get("/ref-data/symbols?token=" + API_TOKEN)
            .then()
            .assertThat()
            .statusCode(SC_OK)
            .body("$", hasItems(hasEntry("symbol", "AAPL")));
    }
}
