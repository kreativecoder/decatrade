package com.decagon.decatrade.exception;

import com.decagon.decatrade.dto.Response;
import lombok.Getter;
import lombok.Setter;

@Getter
public class AbstractException extends RuntimeException {
    Response response;

    public AbstractException(String code, String message) {
        super(message);
        this.response = new Response(code, message);
    }
}