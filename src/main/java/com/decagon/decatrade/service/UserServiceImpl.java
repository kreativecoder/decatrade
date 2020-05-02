package com.decagon.decatrade.service;

import com.decagon.decatrade.dto.UserDto;
import com.decagon.decatrade.model.User;
import com.decagon.decatrade.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findByUsername(final String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDto save(UserDto userDto) {
        Optional<User> optionalUser = findByUsername(userDto.getUsername());
        if (!optionalUser.isPresent()) {
            User user = new User();
            user.setUsername(userDto.getUsername());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setPassword(userDto.getPassword());

            user = userRepository.save(user);
            return userDto;
        } else {
            throw new RuntimeException("Username exists.");
        }
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
