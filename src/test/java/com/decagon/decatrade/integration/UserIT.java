package com.decagon.decatrade.integration;

import com.decagon.decatrade.dto.UserDto;
import com.decagon.decatrade.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.decagon.decatrade.TestUtils.randomName;
import static com.decagon.decatrade.TestUtils.randomUsername;
import static io.restassured.RestAssured.when;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.is;


public class UserIT extends BaseIT {

    @Autowired
    UserService userService;

    @Test
    public void testCheckUsername() {
        String username = randomUsername();
        //clean state, username available for use
        when().get("/api/v1/users/check?username=" + username)
            .then()
            .assertThat()
            .statusCode(SC_OK)
            .body("code", is("00"),
                "message", is("Success"));

        //insert user in database
        createUser(username);

        //username no longer available
        when().get("/api/v1/users/check?username=" + username)
            .then()
            .assertThat()
            .statusCode(SC_OK)
            .body("code", is("04"),
                "message", is("Username exists."));

        //delete user
        userService.deleteUser(username);
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
