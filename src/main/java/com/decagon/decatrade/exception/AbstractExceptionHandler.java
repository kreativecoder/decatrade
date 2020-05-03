package com.decagon.decatrade.exception;

import com.decagon.decatrade.dto.Response;
import com.decagon.decatrade.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
        log.error("BadRequestException >>> " + ex);
        return new ResponseEntity<>(ex.getResponse(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Response> constraintViolation(BadCredentialsException ex) {
        log.error("BadCredentialsException >>> " + ex);
        return new ResponseEntity<>(new Response(Constants.FORMAT_ERROR_CODE, ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response> constraintViolation(NotFoundException ex) {
        log.error("NotFoundException >>> " + ex);
        return new ResponseEntity<>(ex.getResponse(), HttpStatus.NOT_FOUND);
    }
}