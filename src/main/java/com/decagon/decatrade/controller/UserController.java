package com.decagon.decatrade.controller;

import com.decagon.decatrade.dto.Response;
import com.decagon.decatrade.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("check")
    public Response checkUserName(@RequestParam(value = "username") String username) {
        Response response;
        if (userService.isUsernameAvailable(username)) {
            response = new Response("00", "Success");
        } else {
            response = new Response("04", "Username exists.");
        }

        return response;
    }
}
