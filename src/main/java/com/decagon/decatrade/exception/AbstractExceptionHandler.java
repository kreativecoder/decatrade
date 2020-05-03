package com.decagon.decatrade.exception;

import com.decagon.decatrade.dto.Response;
import com.decagon.decatrade.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class AbstractExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> constraintViolation(Exception ex) {
        log.error("General Exception >>> " + ex);
        return new ResponseEntity<>(new Response(Constants.SERVER_ERROR_CODE, Constants.SERVER_ERROR_MESSAGE),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response> constraintViolation(BadRequestException ex) {
        log.error("BadRequestException Exception >>> " + ex);
        return new ResponseEntity<>(ex.getResponse(), HttpStatus.BAD_REQUEST);
    }
}