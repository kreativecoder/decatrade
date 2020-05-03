package com.decagon.decatrade.service;

import com.decagon.decatrade.dto.UserDto;
import com.decagon.decatrade.exception.BadRequestException;
import com.decagon.decatrade.model.User;
import com.decagon.decatrade.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.decagon.decatrade.TestUtils.randomName;
import static com.decagon.decatrade.TestUtils.randomUsername;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    public void testUsernameIsValid() {
        String username = randomUsername();
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThat(userService.isUsernameAvailable(username)).isTrue();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));
        assertThat(userService.isUsernameAvailable(username)).isFalse();
    }

    @Test
    public void testSaveUser() {
        UserDto userDto = UserDto.builder().
            firstName(randomName())
            .lastName(randomName())
            .username(randomUsername())
            .password(randomName())
            .build();

        //username already exists
        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.of(new User()));
        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.save(userDto));
        assertEquals(exception.getMessage(), "Username exists.");


        //username does not exist, user can be saved.
        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.empty());
        UserDto saved = userService.save(userDto);
        assertEquals(saved.getUsername(), userDto.getUsername());
    }

}
