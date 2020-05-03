package com.decagon.decatrade.integration;

import com.decagon.decatrade.dto.UserDto;
import com.decagon.decatrade.service.UserService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static com.decagon.decatrade.TestUtils.randomName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIT {
    private static final String BASE_URI = "http://localhost/api/v1/";

    @Value("${local.server.port}")
    private int port;

    @Autowired
    UserService userService;

    @BeforeAll
    void setup() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    public UserDto createUser(String username) {
        UserDto userDto = UserDto.builder().
            firstName(randomName())
            .lastName(randomName())
            .username(username)
            .password(randomName())
            .build();

        return userService.save(userDto);
    }

    public void deleteUser(String username) {
        userService.deleteUser(username);
    }
}
