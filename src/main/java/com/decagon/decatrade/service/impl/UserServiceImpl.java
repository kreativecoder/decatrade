package com.decagon.decatrade.service.impl;

import com.decagon.decatrade.dto.UserDto;
import com.decagon.decatrade.exception.BadRequestException;
import com.decagon.decatrade.model.User;
import com.decagon.decatrade.repository.UserRepository;
import com.decagon.decatrade.security.JwtTokenProvider;
import com.decagon.decatrade.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Optional<User> findByUsername(final String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDto save(UserDto userDto) {
        Optional<User> optionalUser = findByUsername(userDto.getUsername());
        if (!optionalUser.isPresent()) {
            User user = new User(userDto.getUsername(), userDto.getFirstName(), userDto.getLastName(),
                passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(user);
            return userDto;
        } else {
            throw new BadRequestException("Username exists.");
        }
    }

    @Override
    public String authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean isUsernameAvailable(final String username) {
        return !findByUsername(username).isPresent();
    }

    @Override
    public void deleteUser(final String username) {
        Optional<User> optionalUser = findByUsername(username);
        optionalUser.ifPresent(userRepository::delete);
    }
}
