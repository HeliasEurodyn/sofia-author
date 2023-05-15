package com.crm.sofia.exception;

import com.crm.sofia.exception.common.SofiaException;

public class CouldNotSaveException extends SofiaException {

    String reason = "";

    public CouldNotSaveException() {
        super("Could not save");
        this.setCode("4002");
        this.setCategory("4002");
        this.reason = "NOT_FOUND_TABLE";
    }

    public CouldNotSaveException(String message) {
        super(message);
        this.setCategory("Save");
        this.setCode("4002");
        this.setVisible(true);
    }

}
