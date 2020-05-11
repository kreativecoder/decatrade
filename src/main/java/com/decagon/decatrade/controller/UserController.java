package com.decagon.decatrade.controller;

import com.decagon.decatrade.dto.LoginRequest;
import com.decagon.decatrade.dto.Response;
import com.decagon.decatrade.dto.UserDto;
import com.decagon.decatrade.exception.BadRequestException;
import com.decagon.decatrade.exception.NotFoundException;
import com.decagon.decatrade.model.User;
import com.decagon.decatrade.security.CurrentUser;
import com.decagon.decatrade.security.UserPrincipal;
import com.decagon.decatrade.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("check")
    public ResponseEntity<Response> checkUserName(@RequestParam(value = "username") String username) {
        if (userService.isUsernameAvailable(username)) {
            return ok(new Response("00", "Success"));
        } else {
            throw new BadRequestException("Username exists.");
        }
    }

    @PostMapping
    public ResponseEntity<Response> registerUser(@Valid @RequestBody UserDto userDto) {
        userService.save(userDto);
        return new ResponseEntity<>(new Response("00", "User Created."), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String token = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        Map<Object, Object> model = new HashMap<>();
        model.put("token", token);

        return ok(model);
    }

    @GetMapping("/portfolio/summary")
    public ResponseEntity getUserPortfolio(@Valid @RequestBody LoginRequest loginRequest) {
        //summarize portfolio
        return ok("test");
    }

    @GetMapping("current")
    public UserDto getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        Optional<User> optionalUser = userService.findById(currentUser.getId());
        return optionalUser.map(UserDto::fromUser).orElseThrow(() -> new NotFoundException("User not found."));
    }
}
