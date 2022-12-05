package com.crm.sofia.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public abstract class BaseDTO implements Serializable {
    private String id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdOn;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant modifiedOn;

    private Long version;

    private Long shortOrder;
}
