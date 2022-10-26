package com.crm.sofia.exception;

public class ExpressionException extends SofiaException{

    public ExpressionException(String message) {
        super(message);
        this.code = "4000";
        this.category = "4000";
    }



}

