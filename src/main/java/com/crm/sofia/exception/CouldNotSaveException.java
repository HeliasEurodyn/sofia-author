package com.crm.sofia.exception;

public class CouldNotSaveException extends SofiaException{

    String reason = "";

    public CouldNotSaveException() {
        super("Could not save");
        this.code = "4002";
        this.category = "4002";
        this.reason = "NOT_FOUND_TABLE";
    }

}
