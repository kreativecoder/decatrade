package com.decagon.decatrade.exception;


import com.decagon.decatrade.utils.Constants;

public class NotFoundException extends AbstractException {

    public NotFoundException(String message) {
        this(Constants.NOT_FOUND_ERROR_CODE, message);
    }

    public NotFoundException(String code, String message) {
        super(code, message);
    }
}
