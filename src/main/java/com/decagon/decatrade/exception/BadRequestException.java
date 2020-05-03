package com.decagon.decatrade.exception;


import com.decagon.decatrade.utils.Constants;

public class BadRequestException extends AbstractException {

    public BadRequestException(String message) {
        this(Constants.FORMAT_ERROR_CODE, message);
    }

    public BadRequestException(String code, String message) {
        super(code, message);
    }
}
