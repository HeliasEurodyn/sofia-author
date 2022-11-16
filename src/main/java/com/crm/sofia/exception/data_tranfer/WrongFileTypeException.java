package com.crm.sofia.exception.data_tranfer;

import com.crm.sofia.exception.common.SofiaException;

public class WrongFileTypeException extends SofiaException {

    public WrongFileTypeException() {
        super("Invalid File Extension");
        this.setCategory("DATA_TRANSFER");
        this.setCode("003-2");
        this.setVisible(true);
    }

    public WrongFileTypeException(String message) {
        super(message);
        this.setCategory("DATA_TRANSFER");
        this.setCode("003-2");
        this.setVisible(true);
    }

}
