package com.crm.sofia.exception.data_tranfer;

import com.crm.sofia.exception.common.SofiaException;

public class ImportedFileAlreadyExistsException  extends SofiaException {

    public ImportedFileAlreadyExistsException() {
        super("The file you try to import has already been imported");
        this.setCategory("DATA_TRANSFER");
        this.setCode("003-3");
        this.setVisible(true);
    }

    public ImportedFileAlreadyExistsException(String message) {
        super(message);
        this.setCategory("DATA_TRANSFER");
        this.setCode("003-3");
        this.setVisible(true);
    }

}
