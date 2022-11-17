package com.crm.sofia.model.list;

import com.crm.sofia.model.component.ComponentView;

import java.time.Instant;

public interface ListView{
    String getId();
    String getName();
    Instant getCreatedOn();
    ComponentView getComponent();

}

