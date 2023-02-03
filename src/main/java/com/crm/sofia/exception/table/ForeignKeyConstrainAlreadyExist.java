package com.crm.sofia.exception.table;

import com.crm.sofia.exception.common.SofiaException;

public class ForeignKeyConstrainAlreadyExist extends SofiaException {

    public ForeignKeyConstrainAlreadyExist() {
        super("ForeignKey Constrain Already Exists");
        this.setCategory("DATABASE");
        this.setCode("004-1");
        this.setVisible(true);
    }

    public ForeignKeyConstrainAlreadyExist(String message) {
        super(message);
        this.setCategory("DATABASE");
        this.setCode("004-1");
        this.setVisible(true);
    }

}
