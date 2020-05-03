package com.decagon.decatrade.integration;

import com.decagon.decatrade.dto.UserDto;
import com.decagon.decatrade.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.decagon.decatrade.TestUtils.randomName;
import static com.decagon.decatrade.TestUtils.randomUser;
import static com.decagon.decatrade.TestUtils.randomUsername;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.is;


public class UserIT extends BaseIT {

    @Autowired
    UserService userService;

    @Test
    public void testCheckUsername() {
        String username = randomUsername();
        //clean state, username available for use
        get("/api/v1/users/check?username=" + username)
            .then()
            .assertThat()
            .statusCode(SC_OK)
            .body("code", is("00"),
                "message", is("Success"));

        //insert user in database
        createUser(username);

        //username no longer available
        get("/api/v1/users/check?username=" + username)
            .then()
            .assertThat()
            .statusCode(SC_BAD_REQUEST)
            .body("code", is("30"),
                "message", is("Username exists."));

        //delete user
        userService.deleteUser(username);
    }

    @Test
    public void testUserRegistration() {
        UserDto userDto = randomUser();

        given()
            .contentType(JSON)
            .body(userDto)
            .post("/api/v1/users")
            .then()
            .assertThat()
            .statusCode(SC_CREATED)
            .body("code", is("00"),
                "message", is("User Created."));

        //delete user
        userService.deleteUser(userDto.getUsername());
    }

    private UserDto createUser(String username) {
        UserDto userDto = UserDto.builder().
            firstName(randomName())
            .lastName(randomName())
            .username(username)
            .password(randomName())
            .build();

        return userService.save(userDto);
    }
}
