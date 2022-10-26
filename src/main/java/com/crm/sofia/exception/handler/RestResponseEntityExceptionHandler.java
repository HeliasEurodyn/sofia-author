package com.crm.sofia.exception.handler;

import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.exception.ExpressionException;
import com.crm.sofia.exception.SofiaException;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler{

    @ExceptionHandler({ExpressionException.class, DoesNotExistException.class})
    public ResponseEntity<Map<String,String>> handleException(SofiaException exception){
        Map<String,String> response = new HashMap<>();
        response.put("code", exception.getCode());
        response.put("category", exception.getCategory());
        response.put("message", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
