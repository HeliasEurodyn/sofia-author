package com.crm.sofia.exception.data_tranfer;

import com.crm.sofia.exception.common.SofiaException;

public class DataImportException extends SofiaException {


    public DataImportException() {
        super("Import Failed");
        this.setCategory("DATA_TRANSFER");
        this.setCode("003-1");
        this.setVisible(true);
    }

    public DataImportException(String message) {
        super(message);
        this.setCategory("DATA_TRANSFER");
        this.setCode("003-1");
        this.setVisible(true);
    }

}
