package com.crm.sofia.exception.handler;

import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.exception.ExpressionException;
import com.crm.sofia.exception.common.SofiaException;
import com.crm.sofia.exception.login.IncorrectPasswordException;
import com.crm.sofia.exception.login.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler{

    @ExceptionHandler(
            {ExpressionException.class, DoesNotExistException.class, UserNotFoundException.class, IncorrectPasswordException.class})
    public ResponseEntity<Map<String,String>> handleException(SofiaException exception){
        Map<String,String> response = new HashMap<>();
        response.put("code", exception.getCode());
        response.put("message", exception.getMessage());
        response.put("category",exception.getCategory());
        response.put("isVisible",Boolean.toString(exception.isVisible()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
