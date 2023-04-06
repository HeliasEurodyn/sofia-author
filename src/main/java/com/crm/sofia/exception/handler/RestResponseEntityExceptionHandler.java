package com.crm.sofia.exception.handler;

import com.crm.sofia.exception.CouldNotSaveException;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.exception.ExpressionException;
import com.crm.sofia.exception.common.SofiaException;
import com.crm.sofia.exception.data_tranfer.DataImportException;
import com.crm.sofia.exception.data_tranfer.ImportedFileAlreadyExistsException;
import com.crm.sofia.exception.data_tranfer.WrongFileTypeException;
import com.crm.sofia.exception.login.IncorrectPasswordException;
import com.crm.sofia.exception.login.UserNotFoundException;
import com.crm.sofia.exception.table.ForeignKeyConstrainAlreadyExist;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(
            {ExpressionException.class, DoesNotExistException.class, UserNotFoundException.class,
                    IncorrectPasswordException.class, DataImportException.class, WrongFileTypeException.class,
                    ImportedFileAlreadyExistsException.class, ForeignKeyConstrainAlreadyExist.class, CouldNotSaveException.class})
    public ResponseEntity<Map<String,String>> handleException(SofiaException exception){
        Map<String,String> response = new HashMap<>();
        response.put("code", exception.getCode());
        response.put("message", exception.getMessage());
        response.put("category",exception.getCategory());
        response.put("isVisible",Boolean.toString(exception.isVisible()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationException(MethodArgumentNotValidException exception){
        Map<String,String> response = new HashMap<>();
        response.put("code", "004-1");
        response.put("message", exception.getBindingResult().getFieldError().getDefaultMessage());
        response.put("category","VALIDATION");
        response.put("isVisible",Boolean.toString(true));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
