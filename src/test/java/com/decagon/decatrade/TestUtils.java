package com.decagon.decatrade;

import com.decagon.decatrade.dto.UserDto;
import org.apache.commons.lang3.RandomStringUtils;

public class TestUtils {
    public static String randomUsername() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public static String randomName() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public static UserDto randomUser() {
        return UserDto.builder().
            firstName(randomName())
            .lastName(randomName())
            .username(randomUsername())
            .password(randomName())
            .build();
    }
}
