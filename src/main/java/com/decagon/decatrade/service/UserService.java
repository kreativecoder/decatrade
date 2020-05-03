package com.decagon.decatrade.service;

import com.decagon.decatrade.dto.UserDto;
import com.decagon.decatrade.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    UserDto save(UserDto userDto);

    String authenticateUser(String username, String password);

    List<User> getAllUsers();

    boolean isUsernameAvailable(String username);

    void deleteUser(String username);
}
