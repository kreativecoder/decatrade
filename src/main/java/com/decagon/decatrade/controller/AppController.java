package com.decagon.decatrade.controller;

import com.decagon.decatrade.dto.UserDto;
import com.decagon.decatrade.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequiredArgsConstructor
public class AppController {
    private final UserService userService;

    @GetMapping("/signup")
    public String signup(WebRequest request, Model model) {
        UserDto user = UserDto.builder().build();
        model.addAttribute("user", user);
        return "signup";
    }
}
