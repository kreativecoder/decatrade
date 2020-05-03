package com.decagon.decatrade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class LoginRequest {
    @NotEmpty
    @Size(min = 5, max = 10)
    private String username;
    @NotEmpty
    @Size(min = 5)
    private String password;
}
