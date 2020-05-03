package com.decagon.decatrade.integration;

import com.decagon.decatrade.dto.LoginRequest;
import com.decagon.decatrade.dto.UserDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.decagon.decatrade.TestUtils.randomUsername;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
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

    @AfterAll
    void tearDown() {
        deleteUser(testUser.getUsername());
    }
}
