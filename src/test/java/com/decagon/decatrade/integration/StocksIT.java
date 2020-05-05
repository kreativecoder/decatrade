package com.decagon.decatrade.integration;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;

public class StocksIT extends TransactionsIT {

    @Test
    public void testGetUserStocks() {
        //user has no stocks

        getStocks()
            .assertThat()
            .statusCode(SC_OK)
            .body("$", hasSize(0));

        //buy stocks
        Map<String, String> request = testTransaction("TSLA", 10, "BUY");
        buyStock(request);
        request = testTransaction("nflx", 5, "BUY");
        buyStock(request);

        getStocks()
            .assertThat()
            .statusCode(SC_OK)
            .body("$", hasSize(greaterThanOrEqualTo(2)));
    }

    private ValidatableResponse getStocks() {
        return given()
            .contentType(JSON)
            .header("Authorization", "Bearer " + authToken)
            .get("stocks")
            .then();
    }

    @AfterAll
    void tearDown() {
        deleteUser(testUser.getUsername());
    }
}
