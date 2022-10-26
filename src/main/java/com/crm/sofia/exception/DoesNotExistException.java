package com.crm.sofia.exception;

public class DoesNotExistException extends SofiaException{

    public DoesNotExistException() {
        super("Could not find user");
        this.code = "4001";
        this.category = "4001";
    }

}
