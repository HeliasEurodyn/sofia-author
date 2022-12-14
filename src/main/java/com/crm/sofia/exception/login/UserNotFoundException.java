package com.crm.sofia.exception.login;

import com.crm.sofia.exception.common.SofiaException;

public class UserNotFoundException extends SofiaException {

    public UserNotFoundException() {
        super("User Not Found");
        this.setCategory("LOGIN");
        this.setCode("001-2");
        this.setVisible(true);
    }

    public UserNotFoundException(String message) {
        super(message);
        this.setCategory("LOGIN");
        this.setCode("001-2");
        this.setVisible(true);
    }
}
