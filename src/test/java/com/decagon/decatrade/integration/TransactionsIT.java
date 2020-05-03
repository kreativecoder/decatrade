package com.decagon.decatrade.integration;

import com.decagon.decatrade.dto.LoginRequest;
import com.decagon.decatrade.dto.UserDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.decagon.decatrade.TestUtils.randomName;
import static com.decagon.decatrade.TestUtils.randomUsername;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TransactionsIT extends BaseIT {

    UserDto testUser;
    String authToken;

    @Override
    @BeforeAll
    void setup() {
        super.setup();

        //create test user
        testUser = createUser(randomUsername());

        //get user token
        authToken = given()
            .contentType(JSON)
            .body(new LoginRequest(testUser.getUsername(), testUser.getPassword()))
            .when().post("users/login")
            .andReturn().jsonPath().getString("token");
        assertNotNull(authToken);
    }

    @Test
    public void testGetLatestPrice() {
        String symbol = "nflx";
        given()
            .contentType(JSON)
            .header("Authorization", "Bearer " + authToken)
            .get("transactions/price/" + symbol)
            .then()
            .assertThat()
            .statusCode(SC_OK)
            .body("symbol", is("NFLX"),
                "companyName", is("Netflix, Inc."),
                "$", hasKey("latestPrice"));
    }

    @Test
    public void testCreateTransaction() {
        String reference = UUID.randomUUID().toString();
        Map<String, String> request = new HashMap<>();
        request.put("symbol", "AAPL");
        request.put("quantity", "10");
        request.put("transactionType", "BUY");
        request.put("reference", reference);

        //create transaction
        given()
            .contentType(JSON)
            .header("Authorization", "Bearer " + authToken)
            .body(request)
            .post("transactions")
            .then()
            .assertThat()
            .statusCode(SC_ACCEPTED)
            .body("reference", is(reference),
                "$", hasKey("totalAmount"));

        //confirm transaction
        given()
            .contentType(JSON)
            .header("Authorization", "Bearer " + authToken)
            .get("transactions/confirm?reference=" + reference)
            .then()
            .assertThat()
            .statusCode(SC_CREATED)
            .body("reference", is(reference),
                "$", hasKey("totalAmount"));

        //confirm non-existent transaction
        given()
            .contentType(JSON)
            .header("Authorization", "Bearer " + authToken)
            .get("transactions/confirm?reference=" + randomName())
            .then()
            .assertThat()
            .statusCode(SC_NOT_FOUND)
            .body("code", is("25"),
                "message", is("Transaction not found."));
    }

    @AfterAll
    void tearDown() {
        deleteUser(testUser.getUsername());
    }
}
