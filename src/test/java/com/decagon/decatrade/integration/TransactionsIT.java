package com.decagon.decatrade.integration;

import com.decagon.decatrade.dto.LoginRequest;
import com.decagon.decatrade.dto.UserDto;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.decagon.decatrade.TestUtils.randomUsername;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
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
    public void testBuyStock() {
        Map<String, String> request = testTransaction("AAPL", 10, "BUY");

        buyStock(request);
    }

    @Test
    public void testSellStock() {
        Map<String, String> request = testTransaction("TSLA", 10, "BUY");

        //buy tesla
        buyStock(request);

        //sell xxx, doesn't exist
        request = testTransaction("xxx", 10, "SELL");
        createTransaction(request)
            .assertThat()
            .statusCode(SC_NOT_FOUND)
            .body("code", is("25"),
                "message", is("User does not own stock."));

        //sell more tsla quantity
        request = testTransaction("TSLA", 100, "SELL");
        createTransaction(request)
            .assertThat()
            .statusCode(SC_BAD_REQUEST)
            .body("code", is("30"),
                "message", is("User position is less than sell quantity."));

        //valid order
        request = testTransaction("TSLA", 5, "SELL");
        createTransaction(request)
            .assertThat()
            .statusCode(SC_ACCEPTED)
            .body("reference", is(request.get("reference")),
                "$", hasKey("totalAmount"));

        confirmTransaction(request.get("reference"))
            .assertThat()
            .statusCode(SC_CREATED)
            .body("reference", is(request.get("reference")),
                "$", hasKey("totalAmount"));
    }

    @Test
    public void testConfirmWrongTransaction() {
        //confirm non-existent transaction
        confirmTransaction("ref")
            .assertThat()
            .statusCode(SC_NOT_FOUND)
            .body("code", is("25"),
                "message", is("Transaction not found."));
    }

    @Test
    public void testCancelTransaction() {
        //cancel non-existent transaction
        cancelTransaction("ref")
            .assertThat()
            .statusCode(SC_NOT_FOUND)
            .body("code", is("25"),
                "message", is("Transaction not found."));


        //cancel already completed transaction
        Map<String, String> request = testTransaction("AAPL", 10, "BUY");
        buyStock(request);
        cancelTransaction(request.get("reference"))
            .assertThat()
            .statusCode(SC_BAD_REQUEST)
            .body("code", is("30"),
                "message", is("Transaction already completed."));


        //cancel pending transaction
        request = testTransaction("AAPL", 10, "BUY");
        createTransaction(request)
            .assertThat()
            .statusCode(SC_ACCEPTED)
            .body("reference", is(request.get("reference")),
                "$", hasKey("totalAmount"));

        cancelTransaction(request.get("reference"))
            .assertThat()
            .statusCode(SC_NO_CONTENT);
    }

    @Test
    public void testGetAllTransactions() {
        Map<String, String> request = testTransaction("AAPL", 10, "BUY");
        buyStock(request);
        request = testTransaction("nflx", 100, "BUY");
        buyStock(request);
        request = testTransaction("nflx", 20, "SELL");
        buyStock(request);

        given()
            .contentType(JSON)
            .header("Authorization", "Bearer " + authToken)
            .get("transactions")
            .then()
            .statusCode(SC_OK)
            .body("$", hasSize(greaterThanOrEqualTo(3))); //cos other tests inserted txns
    }

    public void buyStock(Map<String, String> request) {
        createTransaction(request)
            .assertThat()
            .statusCode(SC_ACCEPTED)
            .body("reference", is(request.get("reference")),
                "$", hasKey("totalAmount"));

        confirmTransaction(request.get("reference"))
            .assertThat()
            .statusCode(SC_CREATED)
            .body("reference", is(request.get("reference")),
                "$", hasKey("totalAmount"));
    }


    private ValidatableResponse createTransaction(Map<String, String> request) {
        //create transaction
        return given()
            .contentType(JSON)
            .header("Authorization", "Bearer " + authToken)
            .body(request)
            .post("transactions")
            .then();
    }

    private ValidatableResponse confirmTransaction(String reference) {
        //confirm transaction
        return given()
            .contentType(JSON)
            .header("Authorization", "Bearer " + authToken)
            .get("transactions/confirm?reference=" + reference)
            .then();
    }

    private ValidatableResponse cancelTransaction(String reference) {
        //confirm transaction
        return given()
            .contentType(JSON)
            .header("Authorization", "Bearer " + authToken)
            .delete("transactions/" + reference)
            .then();
    }

    public Map<String, String> testTransaction(String symbol, long quantity, String type) {
        Map<String, String> request = new HashMap<>();
        request.put("symbol", symbol);
        request.put("quantity", String.valueOf(quantity));
        request.put("transactionType", type);
        request.put("reference", UUID.randomUUID().toString());

        return request;
    }

    @AfterAll
    void tearDown() {
        deleteUser(testUser.getUsername());
    }
}
