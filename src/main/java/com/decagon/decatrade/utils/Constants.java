package com.decagon.decatrade.utils;

public class Constants {
    public static final String SUCCESS_CODE = "00";
    public static final String SUCCESS_MESSAGE = "Successful.";
    public static final String SERVER_ERROR_CODE = "06";
    public static final String SERVER_ERROR_MESSAGE = "General Error.";
    public static final String DUPLICATE_ERROR_CODE = "94";
    public static final String DUPLICATE_ERROR_MESSAGE = "Duplicate Record.";
    public static final String FORMAT_ERROR_CODE = "30";
    public static final String FORMAT_ERROR_MESSAGE = "Format Error.";
    public static final String NOT_FOUND_ERROR_CODE = "25";
    public static final String NOT_FOUND_ERROR_MESSAGE = "Not Found.";
    public static final String REST_CLIENT_ERROR_CODE = "101";

    //security
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "api/v1/users";
}
