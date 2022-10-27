package com.crm.sofia.exception;

import com.crm.sofia.exception.common.SofiaException;

public class DoesNotExistException extends SofiaException {

    public DoesNotExistException() {
        super("Could not find user");
        this.setCode("4001");
        this.setCategory("4001");
    }

}
