package org.example.basedomain.exception;

import org.example.basedomain.Entity.ErrorObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public  class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorObject> UserExceptipn(UserException ex, WebRequest request) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setMessage(ex.getMessage());
        errorObject.setStatusCode(HttpStatus.BAD_GATEWAY.getReasonPhrase());
        errorObject.setTimestamp(new Date());
        return new ResponseEntity<>(errorObject,HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

       String message = ex.getBindingResult().getFieldErrors().stream()
                .map(x->x.getDefaultMessage()).toString();
        ErrorObject errorObject = new ErrorObject(message,status.toString(),new Date());
        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }
}