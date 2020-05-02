package com.decagon.decatrade;

import org.apache.commons.lang3.RandomStringUtils;

public class TestUtils {
    public static String randomUsername() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public static String randomName() {
        return RandomStringUtils.randomAlphabetic(5);
    }
}
