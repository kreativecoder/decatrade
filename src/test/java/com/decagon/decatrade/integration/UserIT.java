package com.decagon.decatrade.integration;

import com.decagon.decatrade.dto.LoginRequest;
import com.decagon.decatrade.dto.UserDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.decagon.decatrade.TestUtils.randomName;
import static com.decagon.decatrade.TestUtils.randomUser;
import static com.decagon.decatrade.TestUtils.randomUsername;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.emptyString;


public class UserIT extends BaseIT {

    @Test
    public void testCheckUsernameSuccess() {
        String username = randomUsername();
        //clean state, username available for use
        get("users/check?username=" + username)
            .then()
            .assertThat()
            .statusCode(SC_OK)
            .body("code", is("00"),
                "message", is("Success"));
    }

    @Test
    public void testCheckUsernameFailed() {
        //insert user in database
        UserDto userDto = createUser(randomUsername());

        //username no longer available
        get("users/check?username=" + userDto.getUsername())
            .then()
            .assertThat()
            .statusCode(SC_BAD_REQUEST)
            .body("code", is("30"),
                "message", is("Username exists."));

        //delete user
        deleteUser(userDto.getUsername());
    }

    @Test
    public void testUserRegistrationSuccessful() {
        UserDto userDto = randomUser();

        given()
            .contentType(JSON)
            .body(userDto)
            .post("users")
            .then()
            .assertThat()
            .statusCode(SC_CREATED)
            .body("code", is("00"),
                "message", is("User Created."));

        //delete user
        deleteUser(userDto.getUsername());
    }

    //TODO: handle constraintviolation exceptions
    @Test
    @Disabled
    public void testUserRegistrationFailed() {
        UserDto userDto = randomUser();
        //username less than required
        userDto.setUsername("abc");

        given()
            .contentType(JSON)
            .body(userDto)
            .post("users")
            .then()
            .assertThat()
            .statusCode(SC_BAD_REQUEST)
            .body("code", is("00"),
                "message", is("User Created."));
    }

    @Test
    public void testUserLoginSuccess() {
        //insert test user in database
        UserDto userDto = createUser(randomUsername());

        //assert successful and token comes back
        given()
            .contentType(JSON)
            .body(new LoginRequest(userDto.getUsername(), userDto.getPassword()))
            .post("users/login")
            .then()
            .assertThat()
            .statusCode(SC_OK)
            .body("token", is(not(emptyString())));

        //delete user
        deleteUser(userDto.getUsername());
    }

    @Test
    public void testUserLoginFailed() {
        //insert test user in database
        UserDto userDto = createUser(randomUsername());

        //invalid password
        given()
            .contentType(JSON)
            .body(new LoginRequest(userDto.getUsername(), randomName()))
            .post("users/login")
            .then()
            .assertThat()
            .statusCode(SC_FORBIDDEN)
            .body("code", is("30"),
                "message", is("Bad credentials"));

        //delete user
        deleteUser(userDto.getUsername());
    }
}
